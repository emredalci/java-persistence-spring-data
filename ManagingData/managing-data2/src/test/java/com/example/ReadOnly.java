package com.example;

import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ReadOnly {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("managing_data2");

    @Test
    void selectiveReadOnly() {
        EntityManager em = emf.createEntityManager();
        FetchTestData testData = storeTestData();
        em.getTransaction().begin();

        Long itemId = testData.items.getFirstId();

        {
            em.unwrap(Session.class).setDefaultReadOnly(true);
            Item item = em.find(Item.class, itemId);
            item.setName("New Name");
            em.flush(); // No update
        }
        {
            em.clear();
            Item item = em.find(Item.class, itemId);
            assertNotEquals("New Name", item.getName());
        }
        {
            Item item = em.find(Item.class, itemId);
            em.unwrap(Session.class).setReadOnly(item, true);
            item.setName("New Name");
            em.flush(); // No update
        }
        {
            em.clear();
            Item item = em.find(Item.class, itemId);
            assertNotEquals("New Name", item.getName());
        }
        {
            Query<Item> query = em.unwrap(Session.class).createQuery("select i from Item i", Item.class);
            query.setReadOnly(true).list();
            List<Item> results = query.list();

            for (Item item : results) {
                item.setName("New Name");
            }
            em.flush(); // No update
        }
        {
            List<Item> items = em.createQuery("select i from Item i", Item.class).setHint(QueryHints.READ_ONLY, Boolean.TRUE).getResultList();

            for (Item item:items) {
                item.setName("New Name");
            }
            em.flush(); // No update
        }
        {
            em.clear();
            Item item = em.find(Item.class, itemId);
            assertNotEquals("New Name", item.getName());
        }
        em.getTransaction().commit();
        em.close();
    }

    @Test
    void immutableEntity() {
        EntityManager em = emf.createEntityManager();
        FetchTestData testData = storeTestData();
        em.getTransaction().begin();

        Long itemId = testData.items.getFirstId();
        Item item = em.find(Item.class, itemId);

        for (Bid bid : item.getBids()) {
            bid.setAmount(new BigDecimal("99.99"));
        }
        em.flush();
        em.clear();

        item = em.find(Item.class, itemId);
        for (Bid bid : item.getBids()) {
            assertNotEquals("99.99", bid.getAmount().toString());
        }
        em.getTransaction().commit();
        em.close();
    }

    private FetchTestData storeTestData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

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

        Item item = new Item("Item One", LocalDate.now().plusDays(1), johndoe);
        em.persist(item);
        itemIds[0] = item.getId();

        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(item, robertdoe, new BigDecimal(9 + i));
            item.addBid(bid);
            em.persist(bid);
        }

        item = new Item("Item Two", LocalDate.now().plusDays(1), johndoe);
        em.persist(item);
        itemIds[1] = item.getId();

        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, janeroe, new BigDecimal(2 + i));
            item.addBid(bid);
            em.persist(bid);
        }

        item = new Item("Item Three", LocalDate.now().plusDays(2), janeroe);
        em.persist(item);
        itemIds[2] = item.getId();

        em.getTransaction().commit();
        em.close();

        FetchTestData testData = new FetchTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }
}
