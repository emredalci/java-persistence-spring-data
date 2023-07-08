package com.example;

import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

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
            if (Objects.nonNull(retrievedItem)) {
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

    @Test
    void retrievePersistenceReference() {

        EntityManager em = emf.createEntityManager(); //Application-managed
        em.getTransaction().begin();
        Item item = new Item();
        item.setName("Some item");
        em.persist(item);
        em.getTransaction().commit();
        Long itemId = item.getId();
        /*
        If the persistence context already contains an Item with the given identifier, that
        Item instance is returned by getReference() without hitting the database.
         */
        Item retrievedItem = em.getReference(Item.class, itemId);
        assertTrue(Objects.nonNull(retrievedItem.getId()));
        em.close();

        {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            /*
            Furthermore, if no persistent instance with that identifier is currently managed, a hollow
            placeholder will be produced by Hibernate, a proxy. This means getReference() will not
            access the database, and it doesn't return null, unlike find().
             */
            Item retrievedItem2 = em.getReference(Item.class, itemId); // The proxy is produced by Hibernate. The Values are null
            assertTrue(Objects.nonNull(retrievedItem2));

            /*
            JPA offers PersistenceUnitUtil helper methods such as isLoaded() to
            detect if you are working with an uninitialized proxy.
            */
            PersistenceUnitUtil persistenceUtil = emf.getPersistenceUnitUtil();
            assertFalse(persistenceUtil.isLoaded(retrievedItem2));

            /*
            As soon as you call any method such as Item#getName() on the proxy, a
            SELECT is executed to fully initialize the placeholder. The exception to this rule is
            a method that is a mapped database identifier getter method, such as getId(). A proxy
            might look like the real thing but it is only a placeholder carrying the identifier value of the
            entity instance it represents. If the database record doesn't exist anymore when the proxy is
            initialized, an EntityNotFoundException will be thrown.
             */
            //assertEquals("Some item", retrievedItem2.getName());

            /*
            Hibernate has a convenient static initialize() method, loading the proxy's data.
            Hibernate.initialize(retrievedItem2);
             */
            em.getTransaction().commit();
            em.close();
            /*
            After the persistence context is closed, item is in detached state. If you do
            not initialize the proxy while the persistence context is still open, you get a
            LazyInitializationException if you access the proxy. You can't load
            data on-demand once the persistence context is closed. The solution is simple: Load the
            data before you close the persistence context.
             */
            assertThrows(LazyInitializationException.class, () -> retrievedItem2.getName());
        }
    }

    @Test
    void makeTransient() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Item someItem = new Item();
        someItem.setName("Some item");
        em.persist(someItem);
        em.getTransaction().commit();
        em.close();
        Long itemId = someItem.getId();

        em = emf.createEntityManager();
        em.getTransaction().begin();


        /*
        If you call find(), Hibernate will execute a SELECT to
        load the Item. If you call getReference(), Hibernate
        will attempt to avoid the SELECT and return a proxy.
         */
        Item item = em.find(Item.class, itemId);
        //Item item = em.getReference(Item.class, itemId);

        /*
        Calling remove() will queue the entity instance for deletion when
        the unit of work completes, it is now in removed state. If remove()
        is called on a proxy, Hibernate will execute a SELECT to load the data.
        An entity instance has to be fully initialized during life cycle transitions. You may
        have life cycle callback methods or an entity listener enabled, and the instance must pass through these
        interceptors to complete its full life cycle.
         */
        em.remove(item);

        /*
        An entity in removed state is no longer in persistent state
         */
        assertFalse(em.contains(item));

        /*
        You cane make the removed instance persistent again, cancelling the deletion
         */
        //em.persist(item);

        // hibernate.use_identifier_rollback was enabled, it now looks like a transient instance
        assertNull(item.getId());

        /*
        When the transaction commits, Hibernate synchronizes the state transitions with the
        database and executes the SQL DELETE. The JVM garbage collector detects that the
        item is no longer referenced by anyone and finally deletes the last trace of
        the data.
         */
        em.getTransaction().commit(); // Flush: Dirty check and SQL UPDATE
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        item = em.find(Item.class, itemId);
        assertNull(item);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    void refresh() throws ExecutionException, InterruptedException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Item someItem = new Item();
        someItem.setName("Some item");
        em.persist(someItem);
        em.getTransaction().commit();
        em.close();
        Long itemId = someItem.getId();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Item item = em.find(Item.class, itemId);
        item.setName("Some name");

        Executors.newSingleThreadExecutor().submit(() -> {
            EntityManager em1 = emf.createEntityManager();
            try {
                em1.getTransaction().begin();
                Session session = em1.unwrap(Session.class);
                session.doWork(connection -> {
                    Item item1 = em1.find(Item.class, itemId);
                    item1.setName("Concurrent Update Name");
                });

                em1.getTransaction().commit();
                em1.close();
            } catch (Exception exception){
                throw new RuntimeException("Concurrent operation failure : " + exception, exception);
            }
            return null;
        }).get();

        em.refresh(item);
        em.getTransaction().commit(); // Flush: Dirty check and SQL Update
        em.close();
        assertEquals("Concurrent Update Name", item.getName());
    }

}
