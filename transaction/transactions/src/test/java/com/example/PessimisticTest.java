package com.example;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PessimisticTest extends OptimisticTest {

    @Test
    void pessimisticReadWrite() throws Exception {
        ConcurrencyTestData testData = storeCategoriesAndItems();
        Long[] categories = testData.getCategories().getIdentifiers();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Long categoryId : categories) {
            /*
            For each Category, query all Item instances in
            PESSIMISTIC_READ lock mode. Hibernate will lock the rows in
            the database with the SQL query. If possible, wait for 5 seconds if some
            other transaction already holds a conflicting lock. If the lock can't
            be obtained, the query throws an exception.
            */
            List<Item> items = em.createQuery("select i from Item i where i.category.id = :categoryId", Item.class)
                    .setLockMode(LockModeType.PESSIMISTIC_READ)
                    .setHint("javax.persistence.lock.timeout", 5000)
                    .setParameter("categoryId", categoryId)
                    .getResultList();

            /*
            If the query returns successfully, you know that you hold an exclusive lock
            on the data and no other transaction can access it with an exclusive lock or
            modify it until this transaction commits.
            */
            for (Item item : items) {
                totalPrice = totalPrice.add(item.getBuyNowPrice());
            }

            /*
            Now a concurrent transaction will try to obtain a write lock, it fails because
            we hold a read lock on the data already. Note that on H2 there actually are no
            read or write locks, only exclusive locks.
            */
            if (categoryId.equals(testData.getCategories().getFirstId())) {

                Executors.newSingleThreadExecutor().submit(() -> {
                    try {

                        EntityManager em1 = emf.createEntityManager();
                        em1.getTransaction().begin();

                        em1.unwrap(Session.class).doWork(connection -> {
                            connection.createStatement().execute("set statement_timeout = 5000");
                        });

                        // Moving the first item from the first category into the last category
                        // This query should fail as someone else holds a lock on the rows.
                        List<Item> items1 = em1.createQuery("select i from Item i where i.category.id = :categoryId", Item.class)
                                .setParameter("categoryId", categoryId)
                                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                .setHint("javax.persistence.lock.timeout", 500)
                                .getResultList();

                        Category lastCategory = em1.getReference(Category.class, testData.getCategories().getLastId()); //Persistence context cache

                        items1.iterator().next().setCategory(lastCategory);
                        em1.getTransaction().commit();
                        em1.close();
                    } catch (Exception e) {
                        assertTrue(e instanceof PersistenceException);
                    }
                }).get();
            }


        }
        /*
        Locks will be released after commit, when the transaction completes
        */
        em.getTransaction().commit();
        em.close();

        assertEquals(0, totalPrice.compareTo(new BigDecimal("108")));

    }

    @Test
    void findLock() {
        ConcurrencyTestData testData = storeCategoriesAndItems();
        Long firstCategoryId = testData.getCategories().getFirstId();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.lock.timeout", 5000);

        // Executes a SELECT .. FOR UPDATE WAIT 5000
        Category category = em.find(Category.class, firstCategoryId, LockModeType.PESSIMISTIC_WRITE, hints);
        category.setName("New name");

        em.getTransaction().commit();
        em.close();
    }

}
