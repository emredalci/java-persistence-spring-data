package com.example;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;

import static org.junit.jupiter.api.Assertions.*;

class NoTransactionalTest extends OptimisticTest {

    @Test
    void autoCommit() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Item someItem = new Item("Original Name");
        em.persist(someItem);
        em.getTransaction().commit();
        em.close();
        Long itemId = someItem.getId();

        //Reading data in auto-commit mode
        {
            /*
            No transaction is active when we create the EntityManager. The
            persistence context is now in a special unsynchronized mode, Hibernate
            will not flush automatically at any time.
            */
            em = emf.createEntityManager();

            /*
            You can access the database to read data; this operation will execute a
            SELECT statement, sent to the database in auto-commit mode.
            */

            Item item = em.find(Item.class, itemId);
            item.setName("New Name");

            /*
            Usually Hibernate would flush the persistence context when you execute a
            Query. However, because the context is unsynchronized,
            flushing will not occur and the query will return the old, original database
            value. Queries with scalar results are not repeatable, you'll see whatever
            values are present in the database and given to Hibernate in the
            ResultSet Note that this isn't a repeatable read either if you are in synchronized mode.
             */
            assertEquals("Original Name",
                    em.createQuery("select i.name from Item i where i.id = :id", String.class)
                            .setParameter("id", itemId).getSingleResult()
            );

            /*
            Retrieving a managed entity instance involves a lookup, during JDBC
            result set marshaling, in the current persistence context. The
            already loaded Item instance with the changed name will
            be returned from the persistence context, values from the database
            will be ignored. This is a repeatable read of an entity instance,
            even without a system transaction.
            */
            assertEquals(
                    "New Name",
                    em.createQuery("select i from Item i where i.id = :id", Item.class)
                            .setParameter("id", itemId).getSingleResult().getName()
            );

            /*
            If you try to flush the persistence context manually, to store the new
            Item#name, Hibernate will throw a javax.persistence.TransactionRequiredException. YOU ARE
            PREVENTED FROM EXECUTING AN UPDATE STATEMENT IN SYNCHRONIZED mode, as you wouldn't be able to roll back the change.
            */
            assertThrows(TransactionRequiredException.class, em::flush);

            /*
            You can roll back the change you made with the refresh()
            method, it loads the current Item state from the database and overwrites the change you have made in memory.
             */
            em.refresh(item);
            assertEquals("Original Name", item.getName());

            em.close();
        }

        // Queueing modifications
        {
            em = emf.createEntityManager();

            Item newItem = new Item("New Item");
            /*
               You can call persist() to save a transient entity instance with an
               unsynchronized persistence context. Hibernate will only fetch a new identifier
               value, typically by calling a database sequence, and assign it to the instance.
               The instance is now in persistent state in the context but the SQL
               INSERT hasn't happened. Note that this is only possible with pre-insert identifier generators
            */
            em.persist(newItem);
            assertNotNull(newItem.getId());

            /*
               When you are ready to store the changes, join the persistence context with
               a transaction. Synchronization and flushing will occur as usual, when the
               transaction commits. Hibernate writes all queued operations to the database.
             */
            em.getTransaction().begin();
            if (Boolean.FALSE.equals(em.isJoinedToTransaction())) {
                em.joinTransaction();
            }
            em.getTransaction().commit(); // Flush!
            em.close();
        }

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            assertEquals("Original Name", em.find(Item.class, itemId).getName());
            assertEquals(2L, em.createQuery("select count(i) from Item i").getSingleResult());
            em.getTransaction().commit();
            em.close();
        }

        // Queueing merged changes of a detached entity
        {
            EntityManager tmp = emf.createEntityManager();
            Item detachedItem = tmp.find(Item.class, itemId);
            tmp.close();

            detachedItem.setName("New Name");
            em = emf.createEntityManager();

            Item mergedItem = em.merge(detachedItem);
            em.getTransaction().begin();
            em.joinTransaction();
            em.getTransaction().commit(); // Flush!
            em.close();
        }

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            assertEquals("New Name", em.find(Item.class, itemId).getName());
            em.getTransaction().commit();
            em.close();
        }

        // Queueing removal of entity instances and DELETE operations
        {
            em = emf.createEntityManager();

            Item item = em.find(Item.class, itemId);
            em.remove(item);

            em.getTransaction().begin();
            em.joinTransaction();
            em.getTransaction().commit(); // Flush!
            em.close();
        }

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            assertEquals(1L, em.createQuery("select count(i) from Item i").getSingleResult());
            em.getTransaction().commit();
            em.close();
        }
    }

}
