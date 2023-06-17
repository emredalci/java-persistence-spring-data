package com.example.mappingassociations.bidirectional;

import com.example.mappingassociations.MappingAssociationsApplication;
import com.example.mappingassociations.onetomany.bidirectional.Bid;
import com.example.mappingassociations.onetomany.bidirectional.Item;
import com.example.mappingassociations.repositories.bidirectional.BidRepository;
import com.example.mappingassociations.repositories.bidirectional.ItemRepository;
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
class BidirectionalTest {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);


    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BidRepository bidRepository;

    @Test
    void test() {
        Item item = new Item("Foo");
        Bid bid = new Bid(item, BigDecimal.valueOf(100));
        Bid bid2 = new Bid(item, BigDecimal.valueOf(200));

        itemRepository.save(item);
        item.addBid(bid);
        item.addBid(bid2);
        bidRepository.save(bid);
        bidRepository.save(bid2);

        List<Item> items = itemRepository.findAll();
        Set<Bid> bids = bidRepository.findByItem(item);

        assertAll(
                () -> assertEquals(1, items.size()),
                () -> assertEquals(2, bids.size())
        );
    }
}
