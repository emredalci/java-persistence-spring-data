package com.example;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTransitionsTest {

    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("managing_data");

    @Test
    void makePersistent() {
        EntityManager em = emf.createEntityManager(); // Application-managed

        em.getTransaction().begin();
        Item item = new Item();
        item.setName("Some Item"); // Item#name is NOT NULL!
        em.persist(item);

        Long itemId = item.getId();

        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        assertEquals("Some Item", em.find(Item.class, itemId).getName());
        em.getTransaction().commit();
        em.close();
    }

    @Test
    void retrievePersistent() {
        EntityManager em = emf.createEntityManager(); // Application-managed
        em.getTransaction().begin();
        Item item = new Item();
        item.setName("Some item");
        em.persist(item);
        em.getTransaction().commit();
        em.close();
        Long itemId = item.getId();
        assertEquals(1, itemId);

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Item retrievedItem = em.find(Item.class, itemId);// Hit the database if not already in persistence context
            if (Objects.nonNull(retrievedItem)){
                retrievedItem.setName("New Name");
            }
            em.getTransaction().commit(); // Flush: Dirty check and SQL UPDATE
            em.close();
            assertTrue(Objects.nonNull(retrievedItem));
        }

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Item item1 = em.find(Item.class, itemId);
            Item item2 = em.find(Item.class, itemId); // Repeatable read, Acts as first-level cache

            assertSame(item1, item2);
            assertTrue(item1.equals(item2));
            assertTrue(item1.getId().equals(item2.getId()));

            em.getTransaction().commit();
            em.close();
        }

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            assertEquals("New Name", em.find(Item.class, itemId).getName());
            em.getTransaction().commit();
            em.close();
        }
    }

}
