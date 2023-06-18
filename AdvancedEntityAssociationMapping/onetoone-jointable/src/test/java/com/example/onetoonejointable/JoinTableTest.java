package com.example.onetoonejointable;

import com.example.onetoonejointable.model.Item;
import com.example.onetoonejointable.model.Shipment;
import com.example.onetoonejointable.repository.ItemRepository;
import com.example.onetoonejointable.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OnetooneJointableApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class JoinTableTest {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    void test() {
        Shipment shipment = new Shipment();
        shipmentRepository.save(shipment);

        Item item = new Item("Foo");
        itemRepository.save(item);

        Shipment auctionShipment = new Shipment(item);
        shipmentRepository.save(auctionShipment);

        Item item2 = itemRepository.findById(item.getId()).get();
        Shipment shipment2 = shipmentRepository.findById(shipment.getId()).get();
        Shipment auctionShipment2 = shipmentRepository.findShipmentWithItem(auctionShipment.getId());

        assertAll(
                () -> assertNull(shipment2.getAuction()),
                () -> assertEquals(item2.getId(), auctionShipment2.getAuction().getId()),
                () -> assertEquals(item2.getName(), auctionShipment2.getAuction().getName())
        );
    }
}
