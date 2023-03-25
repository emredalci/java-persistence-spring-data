package com.example.springdatajpa;

import com.example.springdatajpa.model.User;
import com.example.springdatajpa.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringDataJpaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractTestContainer {

    @Autowired
    UserRepository userRepository;

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES_SQL_CONTAINER::getDriverClassName);
    }


    @BeforeEach
    void setUp() {
        userRepository.saveAll(generateUsers());
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }


    static List<User> generateUsers() {
        List<User> users = new ArrayList<>();

        User john = new User("john",
                LocalDate.of(2020, Month.APRIL, 13),
                "john@somedomain.com",
                1,
                true);

        User mike = new User("mike",
                LocalDate.of(2020, Month.JANUARY, 18),
                "mike@somedomain.com",
                3,
                true);

        User james = new User("james",
                LocalDate.of(2020, Month.MARCH, 11),
                "james@someotherdomain.com",
                3,
                false);

        User katie = new User("katie",
                LocalDate.of(2021, Month.JANUARY, 5),
                "katie@somedomain.com",
                5,
                true);

        User beth = new User("beth",
                LocalDate.of(2020, Month.AUGUST, 3),
                "beth@somedomain.com",
                2,
                true);

        User julius = new User("julius",
                LocalDate.of(2021, Month.FEBRUARY, 9),
                "julius@someotherdomain.com",
                4,
                true);

        User darren = new User("darren",
                LocalDate.of(2020, Month.DECEMBER, 11),
                "darren@somedomain.com",
                2,
                true);

        User marion = new User("marion",
                LocalDate.of(2020, Month.SEPTEMBER, 23),
                "marion@someotherdomain.com",
                2,
                false);

        User stephanie = new User("stephanie",
                LocalDate.of(2020, Month.JANUARY, 18),
                "stephanie@someotherdomain.com",
                4,
                true);

        User burk = new User("burk",
                LocalDate.of(2020, Month.NOVEMBER, 28),
                "burk@somedomain.com",
                1,
                true);

        users.add(john);
        users.add(mike);
        users.add(james);
        users.add(katie);
        users.add(beth);
        users.add(julius);
        users.add(darren);
        users.add(marion);
        users.add(stephanie);
        users.add(burk);

        return users;
    }

}
