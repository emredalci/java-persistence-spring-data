package com.example;

import com.example.proxy.Bid;
import com.example.proxy.Category;
import com.example.proxy.Item;
import com.example.proxy.User;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.proxy.HibernateProxyHelper;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProxyTest {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("transactions");

    private FetchTestData storeTestData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Long[] categoryIds = new Long[3];
        Long[] itemIds = new Long[3];
        Long[] userIds = new Long[3];

        User johndoe = new User("johndoe");
        em.persist(johndoe);
        userIds[0] = johndoe.getId();

        User janeroe = new User("janeroe");
        em.persist(janeroe);
        userIds[1] = janeroe.getId();

        User robertdoe = new User("robertdoe");
        em.persist(robertdoe);
        userIds[2] = robertdoe.getId();

        Category category = new Category("Category One");
        em.persist(category);
        categoryIds[0] = category.getId();

        Item item = new Item("Item One", LocalDate.now().plusDays(1), johndoe);
        em.persist(item);
        itemIds[0] = item.getId();
        category.addItem(item);
        item.addCategory(category);
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(item, robertdoe, new BigDecimal(9 + i));
            item.addBid(bid);
            em.persist(bid);
        }

        category = new Category("Category Two");
        em.persist(category);
        categoryIds[1] = category.getId();

        item = new Item("Item Two", LocalDate.now().plusDays(1), johndoe);
        em.persist(item);
        itemIds[1] = item.getId();
        category.addItem(item);
        item.addCategory(category);
        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, janeroe, new BigDecimal(2 + i));
            item.addBid(bid);
            em.persist(bid);
        }

        item = new Item("Item Three", LocalDate.now().plusDays(2), janeroe);
        em.persist(item);
        itemIds[2] = item.getId();
        category.addItem(item);
        item.addCategory(category);

        category = new Category("Category Three");
        em.persist(category);
        categoryIds[2] = category.getId();

        em.getTransaction().commit();
        em.close();

        FetchTestData testData = new FetchTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }

    @Test
    void lazyCollections() {
        FetchTestData testData = storeTestData();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Long itemId = testData.items.getFirstId();

        {
            Item item = em.find(Item.class, itemId);
            // select * from ITEM where ID = ?

            Set<Bid> bids = item.getBids(); // Collection is not initialized
            PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
            assertFalse(persistenceUtil.isLoaded(item, "bids"));

            // It's a Set
            assertTrue(Set.class.isAssignableFrom(bids.getClass()));

            // It's not a HashSet
            assertNotEquals(HashSet.class, bids.getClass());
            assertEquals(org.hibernate.collection.internal.PersistentSet.class, bids.getClass());

            Bid firstBid = bids.iterator().next();
            // select * from BID where ITEM_ID = ?

            // Alternative: Hibernate.initialize(bids);
        }
        em.clear();
        {
            Item item = em.find(Item.class, itemId);
            // select * from ITEM where ID = ?

            assertEquals(3, item.getBids().size());
            // select count(b) from BID b where b.ITEM_ID = ?
        }

        em.getTransaction().commit();
        em.close();
    }

    @Test
    void lazyEntityProxies() {
        FetchTestData testData = storeTestData();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Long itemId = testData.items.getFirstId();
        Long userId = testData.users.getFirstId();

        {
            Item item = em.getReference(Item.class, itemId); // No SELECT

            // Calling identifier getter (no field access!) doesn't trigger initialization
            assertEquals(itemId, item.getId());

            // The class is runtime generated, named something like: Item$HibernateProxy$BLsrPly8
            assertNotEquals(Item.class, item.getClass());

            assertEquals(
                    Item.class,
                    HibernateProxyHelper.getClassWithoutInitializingProxy(item)
            );

            PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
            assertFalse(persistenceUtil.isLoaded(item));
            assertFalse(persistenceUtil.isLoaded(item, "seller"));

            assertFalse(Hibernate.isInitialized(item));
            // Would trigger initialization of item!
            // assertFalse(Hibernate.isInitialized(item.getSeller()));

            Hibernate.initialize(item);
            // select * from ITEM where ID = ?

            // Let's make sure the default EAGER of @ManyToOne has been overriden with LAZY
            assertFalse(Hibernate.isInitialized(item.getSeller()));

            Hibernate.initialize(item.getSeller());
            // select * from USERS where ID = ?
        }
        em.clear();
        {
            /*
            Item entity instance is loaded in the persistence context, its seller is not initialized, it's a User proxy.
            */
            Item item = em.find(Item.class, itemId);
            // select * from ITEM where ID = ?

            /*
            You can manually detach the data from the persistence context, or close the
            persistence context and detach everything.
            */
            em.detach(item);
            em.detach(item.getSeller());
            // em.close();

            /*
            The static PersistenceUtil helper works without a persistence
            context, you can check at any time if the data you want to access has actually been loaded.
            */
            PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
            assertTrue(persistenceUtil.isLoaded(item));
            assertFalse(persistenceUtil.isLoaded(item, "seller"));

            /*
            In detached state, you can call the identifier getter method of the
            User proxy. However, calling any other method on the proxy,
            such as getUsername(), will throw a LazyInitializationException.
            Data can only be loaded on-demand while the persistence context manages the proxy, not in detached state.
            */
            assertEquals(userId, item.getSeller().getId());
            // Throws exception!
            //assertNotNull(item.getSeller().getUsername());
            User seller = item.getSeller();
            assertThrows(LazyInitializationException.class, seller::getUsername);
        }
        em.clear();
        {
            // There is no SQL SELECT in this procedure, only one INSERT!
            Item item = em.getReference(Item.class, itemId);
            User user = em.getReference(User.class, userId);

            Bid newBid = new Bid(new BigDecimal("99.00"));
            newBid.setItem(item);
            newBid.setBidder(user);

            em.persist(newBid);
            // insert into BID values (?, ? ,? , ...)

            em.flush();
            em.clear();
            assertEquals(0, em.find(Bid.class, newBid.getId()).getAmount().compareTo(new BigDecimal("99")));
        }

        em.getTransaction().commit();
        em.close();
    }
}
