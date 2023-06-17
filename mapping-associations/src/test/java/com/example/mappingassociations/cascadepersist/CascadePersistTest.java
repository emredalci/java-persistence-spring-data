package com.example.mappingassociations.cascadepersist;

import com.example.mappingassociations.MappingAssociationsApplication;
import com.example.mappingassociations.onetomany.cascadepersist.Bid;
import com.example.mappingassociations.onetomany.cascadepersist.Item;
import com.example.mappingassociations.repositories.cascadepersist.BidCascadePersistRepository;
import com.example.mappingassociations.repositories.cascadepersist.ItemCascadePersistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MappingAssociationsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CascadePersistTest {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    ItemCascadePersistRepository itemRepository;

    @Autowired
    BidCascadePersistRepository bidRepository;

    @Test
    void test() {
        Item item = new Item("Foo");

        Bid bid = new Bid(BigDecimal.valueOf(100), item);
        Bid bid2 = new Bid(BigDecimal.valueOf(200), item);
        item.addBid(bid);
        item.addBid(bid2);

        itemRepository.save(item);

        List<Item> items = itemRepository.findAll();
        Set<Bid> bids = bidRepository.findByItem(item);

        assertAll(
                () -> assertEquals(1, items.size()),
                () -> assertEquals(2, bids.size())
        );

        Item retrievedItem = itemRepository.findById(item.getId()).get();

        for (Bid someBid : bidRepository.findByItem(retrievedItem)) {
            bidRepository.delete(someBid);
        }

        itemRepository.delete(retrievedItem);

        List<Item> items2 = itemRepository.findAll();
        Set<Bid> bids2 = bidRepository.findByItem(item);

        assertAll(
                () -> assertEquals(0, items2.size()),
                () -> assertEquals(0, bids2.size())
        );
    }
}
