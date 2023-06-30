package com.example.manytomanybidirectional;

import com.example.manytomanybidirectional.model.Category;
import com.example.manytomanybidirectional.model.Item;
import com.example.manytomanybidirectional.repository.CategoryRepository;
import com.example.manytomanybidirectional.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ManytomanyBidirectionalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ManyToManyBidirectionalTest {

    public static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Transactional
    void test() {
        Category someCategory = new Category("Some Category");
        Category otherCategory = new Category("Other Category");

        Item someItem = new Item("Some Item");
        Item otherItem = new Item("Other Item");

        someCategory.addItem(someItem);
        someItem.addCategory(someCategory);

        someCategory.addItem(otherItem);
        otherItem.addCategory(someCategory);

        otherCategory.addItem(someItem);
        someItem.addCategory(otherCategory);

        categoryRepository.save(someCategory);
        categoryRepository.save(otherCategory);

        Category category1 = categoryRepository.findById(someCategory.getId()).get();
        Category category2 = categoryRepository.findById(otherCategory.getId()).get();

        Item item1 = itemRepository.findById(someItem.getId()).get();
        Item item2 = itemRepository.findById(otherItem.getId()).get();

        assertAll(
                () -> assertEquals(2, category1.getItems().size()),
                () -> assertEquals(2, item1.getCategories().size()),
                () -> assertEquals(1, category2.getItems().size()),
                () -> assertEquals(1, item2.getCategories().size()),
                () -> assertEquals(item1, category2.getItems().iterator().next()),
                () -> assertEquals(category1, item2.getCategories().iterator().next())
        );
    }
}
