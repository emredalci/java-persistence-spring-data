package com.example;

import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class Optimistic {

    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("transactions");

    private ConcurrencyTestData storeCategoriesAndItems() {

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        ConcurrencyTestData testData = new ConcurrencyTestData();
        testData.setCategories(new TestData(new Long[3]));
        testData.setItems(new TestData(new Long[5]));

        for (int i = 1; i <= testData.getCategories().getIdentifiers().length; i++) {
            Category category = new Category();
            category.setName("Category: " + i);
            em.persist(category);
            testData.getCategories().getIdentifiers()[i - 1] = category.getId();
            for (int j = 1; j <= testData.getCategories().getIdentifiers().length; j++) {
                Item item = new Item("Item " + j + " of Category " + i);
                item.setCategory(category);
                item.setBuyNowPrice(new BigDecimal(10 + j));
                em.persist(item);
                testData.getItems().getIdentifiers()[(i - 1) + (j - 1)] = item.getId();
            }
        }
        em.getTransaction().commit();
        em.close();
        return testData;
    }

    private TestData storeItemAndBids() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Long[] ids = new Long[1];
        Item item = new Item("Some Item");
        em.persist(item);
        ids[0] = item.getId();
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(new BigDecimal(10 + i), item);
            em.persist(bid);
        }
        em.getTransaction().commit();
        em.close();
        return new TestData(ids);
    }

    @Test
    void firstCommitWins() throws ExecutionException, InterruptedException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Item someItem = new Item("Some Item");
        em.persist(someItem);
        em.getTransaction().commit();
        em.close();
        final Long itemId = someItem.getId();

        EntityManager em1 = emf.createEntityManager();
        em1.getTransaction().begin();

        Item item = em1.find(Item.class, itemId);// select * from ITEM where ID = ?

        //The current version of the Item instance is 0.
        assertEquals(0, item.getVersion());
        item.setName("New Name");

        {
            // The concurrent second unit of work doing the same
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    EntityManager em2 = emf.createEntityManager();
                    em2.getTransaction().begin();

                    Item item1 = em2.find(Item.class, itemId); // select * from ITEM where ID = ?

                    assertEquals(0, item1.getVersion());

                    item1.setName("Other Name");

                    // update ITEM set NAME = ?, VERSION = 1 where ID = ? and VERSION = 0
                    // This succeeds, there is a row with ID = ? and VERSION = 0 in the database!
                    em2.getTransaction().commit();
                    em2.close();

                } catch (Exception ex) {
                    // This shouldn't happen, this commit should win!
                    throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                }
                return null;
            }).get();
        }

        /*
           When the persistence context is flushed Hibernate will detect the dirty
           Item instance and increment its version to 1. The SQL
           UPDATE now performs the version check, storing the new version
           in the database, but only if the database version is still 0.
        */

        // update ITEM set NAME = ?, VERSION = 1 where ID = ? and VERSION = 0
        assertThrows(OptimisticLockException.class, em1::flush);
    }

    @Test
    void manualVersioning() throws ExecutionException, InterruptedException {
        ConcurrencyTestData testData = storeCategoriesAndItems();
        Long[] categories = testData.getCategories().getIdentifiers();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Long categoryId : categories) {

            List<Item> items =
                    em.createQuery("select i from Item i where i.category.id = :catId", Item.class)
                            .setLockMode(LockModeType.OPTIMISTIC)
                            .setParameter("catId", categoryId)
                            .getResultList();

            for (Item item : items)
                totalPrice = totalPrice.add(item.getBuyNowPrice());

            // Now a concurrent transaction will move an item to another category
            if (categoryId.equals(testData.getCategories().getFirstId())) {
                Executors.newSingleThreadExecutor().submit(() -> {
                    try {
                        EntityManager em1 = emf.createEntityManager();
                        em1.getTransaction().begin();

                        // Moving the first item from the first category into the last category
                        List<Item> items1 =
                                em1.createQuery("select i from Item i where i.category.id = :catId", Item.class)
                                        .setParameter("catId", testData.getCategories().getFirstId())
                                        .getResultList();

                        // get lastCategory from persistence context
                        Category lastCategory = em1.getReference(
                                Category.class, testData.getCategories().getLastId()
                        );

                        items1.iterator().next().setCategory(lastCategory);

                        em1.getTransaction().commit();
                        em1.close();
                    } catch (Exception ex) {
                        // This shouldn't happen, this commit should win!
                        throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                    }
                    return null;
                }).get();
            }
        }
        // There is a new version of item 1. Because its category has been moved from 1 to 3.
        assertThrows(RollbackException.class, em.getTransaction()::commit);
        em.close();

    }

    @Test
    void forceIncrement() throws Throwable{

        TestData testData = storeItemAndBids();
        Long itemId = testData.getFirstId();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        /*
           The OPTIMISTIC_FORCE_INCREMENT mode tells Hibernate that the version
           of the retrieved Item should be incremented after loading,
           even if it's never modified in the unit of work.
        */
        Item item = em.find(Item.class, itemId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        Bid highestBid = queryHighestBid(em, item);

        // Now a concurrent transaction will place a bid for this item, and
        // succeed because the first commit wins!
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                EntityManager em1 = emf.createEntityManager();
                em1.getTransaction().begin();

                Item item1 = em1.find(
                        Item.class,
                        testData.getFirstId(),
                        LockModeType.OPTIMISTIC_FORCE_INCREMENT
                );
                Bid highestBid1 = queryHighestBid(em1, item1);

                Bid newBid = new Bid(
                        new BigDecimal("44.44"),
                        item1,
                        highestBid1
                );
                em1.persist(newBid);

                em1.getTransaction().commit();
                em1.close();
            } catch (Exception ex) {
                // This shouldn't happen, this commit should win!
                throw new RuntimeException("Concurrent operation failure: " + ex, ex);
            }
            return null;
        }).get();

        /*
           The code persists a new Bid instance; this does not affect
           any values of the Item instance. A new row will be inserted
           into the BID table. Hibernate would not detect concurrently
           made bids at all without a forced version increment of the
           Item. We also use a checked exception to validate the
           new bid amount; it must be greater than the currently highest bid.
        */
        Bid newBid = new Bid(new BigDecimal("45.45"), item, highestBid);
        em.persist(newBid);

        /*
            When flushing the persistence context, Hibernate will execute an
            INSERT for the new Bid and force an UPDATE of the Item with a version check.
            If someone modified the Item concurrently, or placed a
            Bid concurrently with this procedure, Hibernate throws an exception.
        */
        assertThrows(RollbackException.class, () -> em.getTransaction().commit());
        em.close();

    }

    private Bid queryHighestBid(EntityManager em, Item item) {
        // Can't scroll with cursors in JPA, have to use setMaxResult()
        try {
            return (Bid) em.createQuery(
                            "select b from Bid b" +
                                    " where b.item = :itm" +
                                    " order by b.amount desc"
                    )
                    .setParameter("itm", item)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

}
