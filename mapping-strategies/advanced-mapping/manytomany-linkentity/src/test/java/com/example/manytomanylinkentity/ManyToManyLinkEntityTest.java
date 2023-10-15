package com.example.manytomanylinkentity;

import com.example.manytomanylinkentity.model.CategorizedItem;
import com.example.manytomanylinkentity.model.Category;
import com.example.manytomanylinkentity.model.Item;
import com.example.manytomanylinkentity.repository.CategorizedItemRepository;
import com.example.manytomanylinkentity.repository.CategoryRepository;
import com.example.manytomanylinkentity.repository.ItemRepository;
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

@SpringBootTest(classes = ManytomanyLinkentityApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ManyToManyLinkEntityTest {

    public static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategorizedItemRepository categorizedItemRepository;

    @Test
    @Transactional
    void test() {
        Category someCategory = new Category("Some Category");
        Category otherCategory = new Category("Other Category");

        categoryRepository.save(someCategory);
        categoryRepository.save(otherCategory);

        Item someItem = new Item("Some Item");
        Item otherItem = new Item("Other Item");

        itemRepository.save(someItem);
        itemRepository.save(otherItem);

        CategorizedItem linkOne = new CategorizedItem(
                "John Smith", someCategory, someItem
        );

        CategorizedItem linkTwo = new CategorizedItem(
                "John Smith", someCategory, otherItem
        );

        CategorizedItem linkThree = new CategorizedItem(
                "John Smith", otherCategory, someItem
        );

        categorizedItemRepository.save(linkOne);
        categorizedItemRepository.save(linkTwo);
        categorizedItemRepository.save(linkThree);

        Category category1 = categoryRepository.findById(someCategory.getId()).get();
        Category category2 = categoryRepository.findById(otherCategory.getId()).get();

        Item item1 = itemRepository.findById(someItem.getId()).get();
        Item item2 = itemRepository.findById(otherItem.getId()).get();

        assertAll(
                () -> assertEquals(2, category1.getCategorizedItems().size()),
                () -> assertEquals(2, item1.getCategorizedItems().size()),
                () -> assertEquals(1, category2.getCategorizedItems().size()),
                () -> assertEquals(1, item2.getCategorizedItems().size()),
                () -> assertEquals(item1, category2.getCategorizedItems().iterator().next().getItem()),
                () -> assertEquals(category1, item2.getCategorizedItems().iterator().next().getCategory())
        );
    }

}
