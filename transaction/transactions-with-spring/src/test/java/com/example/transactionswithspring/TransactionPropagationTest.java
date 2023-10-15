package com.example.transactionswithspring;

import com.example.transactionswithspring.exception.DuplicateItemNameException;
import com.example.transactionswithspring.service.ItemService;
import com.example.transactionswithspring.service.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.IllegalTransactionStateException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TransactionsWithSpringApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TransactionPropagationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private LogService logService;

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);


    @Test
    void notSupported() {
        // executing in transaction:
        // addLogs is starting transaction, but addSeparateLogsNotSupported() suspends it
        assertAll(
                () -> assertThrows(RuntimeException.class, itemService::addLogs),
                () -> assertEquals(1, logService.findAll().size()),
                () -> assertEquals("check from not supported 1", logService.findAll().get(0).getMessage())
        );

        // no transaction - first record is added in the log even after exception
        logService.showLogs();
    }

    @Test
    void supports() {
        // executing without transaction:
        // addSeparateLogsSupports is working with no transaction
        assertAll(
                () -> assertThrows(RuntimeException.class, logService::addSeparateLogsSupports),
                () -> assertEquals(1, logService.findAll().size()),
                () -> assertEquals("check from supports 1", logService.findAll().get(0).getMessage())
        );

        // no transaction - first record is added in the log even after exception
        logService.showLogs();
    }

    @Test
    void mandatory() {
        // get exception because checkNameDuplicate can be executed only in transaction
        IllegalTransactionStateException ex = assertThrows(IllegalTransactionStateException.class, () -> itemService.checkNameDuplicate("Item1"));
        assertEquals("No existing transaction found for transaction marked with propagation 'mandatory'", ex.getMessage());
    }

    @Test
    void never() {
        itemService.addItem("Item1", LocalDate.of(2022, 5, 1));
        // it's safe to call showLogs from no transaction
        logService.showLogs();

        // but prohibited to execute from transaction
        IllegalTransactionStateException ex = assertThrows(IllegalTransactionStateException.class, () -> itemService.showLogs());
        assertEquals("Existing transaction found for transaction marked with propagation 'never'", ex.getMessage());
    }

    @Test
    void requiresNew() {
        // requires new - log message is persisted in the logs even after exception
        // because it was added in the separate transaction
        itemService.addItem("Item1", LocalDate.of(2022, 5, 1));
        itemService.addItem("Item2", LocalDate.of(2022, 3, 1));
        itemService.addItem("Item3", LocalDate.of(2022, 1, 1));

        DuplicateItemNameException ex = assertThrows(DuplicateItemNameException.class, () -> itemService.addItem("Item2", LocalDate.of(2016, 3, 1)));
        assertAll(
                () -> assertEquals("Item with name Item2 already exists", ex.getMessage()),
                () -> assertEquals(4, logService.findAll().size()),
                () -> assertEquals(3, itemService.findAll().size())
        );

        System.out.println("Logs: ");
        logService.findAll().forEach(System.out::println);

        System.out.println("List of added items: ");
        itemService.findAll().forEach(System.out::println);
    }

    @Test
    void noRollback() {
        // no rollback - log message is persisted in the logs even after exception
        // because transaction was not rolled back
        itemService.addItemNoRollback("Item1", LocalDate.of(2022, 5, 1));
        itemService.addItemNoRollback("Item2", LocalDate.of(2022, 3, 1));
        itemService.addItemNoRollback("Item3", LocalDate.of(2022, 1, 1));

        DuplicateItemNameException ex = assertThrows(DuplicateItemNameException.class, () -> itemService.addItem("Item2", LocalDate.of(2016, 3, 1)));
        assertAll(
                () -> assertEquals("Item with name Item2 already exists", ex.getMessage()),
                () -> assertEquals(4, logService.findAll().size()),
                () -> assertEquals(3, itemService.findAll().size())
        );

        System.out.println("Logs: ");
        logService.findAll().forEach(System.out::println);

        System.out.println("List of added items: ");
        itemService.findAll().forEach(System.out::println);
    }


}
