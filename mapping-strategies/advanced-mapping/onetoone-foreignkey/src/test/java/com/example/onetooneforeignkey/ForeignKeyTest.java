package com.example.onetooneforeignkey;

import com.example.onetooneforeignkey.model.Address;
import com.example.onetooneforeignkey.model.User;
import com.example.onetooneforeignkey.repository.AddressRepository;
import com.example.onetooneforeignkey.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OnetooneForeignkeyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ForeignKeyTest {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void test() {
        User john = new User("John Smith");

        Address address =
                new Address("Flowers Street", "01246", "Boston");

        john.setShippingAddress(address);

        userRepository.save(john);

        User user = userRepository.findUserWithAddress(john.getId());
        Address address2 = addressRepository.findById(address.getId()).get();

        assertAll(
                () -> assertEquals("Flowers Street", user.getShippingAddress().getStreet()),
                () -> assertEquals("01246", user.getShippingAddress().getZipcode()),
                () -> assertEquals("Boston", user.getShippingAddress().getCity()),
                () -> assertEquals("Flowers Street", address2.getStreet()),
                () -> assertEquals("01246", address2.getZipcode()),
                () -> assertEquals("Boston", address2.getCity())
        );
    }
}
