package com.example.onetomanyjointable;


import com.example.onetomanyjointable.model.Item;
import com.example.onetomanyjointable.model.User;
import com.example.onetomanyjointable.repository.ItemRepository;
import com.example.onetomanyjointable.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = OnetomanyJointableApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class JoinTableTest {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void test() {
        Item someItem = new Item("Some Item");
        itemRepository.save(someItem);
        Item otherItem = new Item("Other Item");
        itemRepository.save(otherItem);

        User someUser = new User("John Smith");
        someUser.getBoughtItems().add(someItem); // Link
        someItem.setBuyer(someUser); // Link
        someUser.getBoughtItems().add(otherItem);
        otherItem.setBuyer(someUser);
        userRepository.save(someUser);

        Item unsoldItem = new Item("Unsold Item");
        itemRepository.save(unsoldItem);

        Item item = itemRepository.findById(someItem.getId()).get();
        Item item2 = itemRepository.findById(unsoldItem.getId()).get();

        assertAll(
                () -> assertEquals("John Smith", item.getBuyer().getUsername()),
                () -> assertTrue(item.getBuyer().getBoughtItems().contains(item)),
                () -> assertNull(item2.getBuyer())
        );
    }
}
