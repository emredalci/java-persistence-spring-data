package com.example.manytomanyternary;

import com.example.manytomanyternary.model.CategorizedItem;
import com.example.manytomanyternary.model.Category;
import com.example.manytomanyternary.model.Item;
import com.example.manytomanyternary.model.User;
import com.example.manytomanyternary.repository.CategoryRepository;
import com.example.manytomanyternary.repository.ItemRepository;
import com.example.manytomanyternary.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ManytomanyTernaryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ManyToManyTernaryTest {

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
    private UserRepository userRepository;

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

        User someUser = new User("John Smith");
        userRepository.save(someUser);

        CategorizedItem linkOne = new CategorizedItem(
                someUser, someItem
        );
        someCategory.addCategorizedItem(linkOne);

        CategorizedItem linkTwo = new CategorizedItem(
                someUser, otherItem
        );
        someCategory.addCategorizedItem(linkTwo);

        CategorizedItem linkThree = new CategorizedItem(
                someUser, someItem
        );
        otherCategory.addCategorizedItem(linkThree);

        Category category1 = categoryRepository.findById(someCategory.getId()).get();
        Category category2 = categoryRepository.findById(otherCategory.getId()).get();

        Item item1 = itemRepository.findById(someItem.getId()).get();
        User john = userRepository.findById(someUser.getId()).get();

        List<Category> categoriesOfItem = categoryRepository.findCategoryWithCategorizedItems(item1);

        assertAll(
                () -> assertEquals(2, category1.getCategorizedItems().size()),
                () -> assertEquals(1, category2.getCategorizedItems().size()),
                () -> assertEquals(item1, category2.getCategorizedItems().iterator().next().getItem()),
                () -> assertEquals(john, category2.getCategorizedItems().iterator().next().getAddedBy()),
                () -> assertEquals(2, categoriesOfItem.size())
        );
    }
}
