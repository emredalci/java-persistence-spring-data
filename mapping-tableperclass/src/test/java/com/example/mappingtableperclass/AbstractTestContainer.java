package com.example.mappingtableperclass;



import com.example.mappingtableperclass.model.BillingDetails;
import com.example.mappingtableperclass.repository.BillingDetailsRepository;
import com.example.mappingtableperclass.repository.CreditCardRepository;
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

@Testcontainers
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MappingTableperclassApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractTestContainer {

    @Autowired BillingDetailsRepository<BillingDetails, Long> billingDetailsRepository;
    @Autowired CreditCardRepository creditCardRepository;

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
}
