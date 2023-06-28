package com.example.onetomanyembeddablejointable;

import com.example.onetomanyembeddablejointable.model.Address;
import com.example.onetomanyembeddablejointable.model.Shipment;
import com.example.onetomanyembeddablejointable.model.User;
import com.example.onetomanyembeddablejointable.repository.ShipmentRepository;
import com.example.onetomanyembeddablejointable.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OnetomanyEmbeddableJointableApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class JoinTableTest {

    public static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    @Transactional
    void test() {
        User user = new User("John Smith");
        Address deliveryAddress = new Address("Flowers Street", "01246", "Boston");
        user.setShippingAddress(deliveryAddress);
        userRepository.save(user);

        Shipment firstShipment = new Shipment();
        deliveryAddress.addDelivery(firstShipment);
        shipmentRepository.save(firstShipment);

        Shipment secondShipment = new Shipment();
        deliveryAddress.addDelivery(secondShipment);
        shipmentRepository.save(secondShipment);

        User johnsmith = userRepository.findById(user.getId()).get();

        assertEquals(2, johnsmith.getShippingAddress().getDeliveries().size());
    }
}
