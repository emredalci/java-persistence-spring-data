package com.example.onetomanybag;

import com.example.onetomanybag.model.Bid;
import com.example.onetomanybag.model.Item;
import com.example.onetomanybag.repository.BidRepository;
import com.example.onetomanybag.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OnetomanyBagApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class OneToManyBagTest {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BidRepository bidRepository;

    @Test
    void test() {

        Item item = new Item("Foo");
        itemRepository.save(item);

        Bid someBid = new Bid(new BigDecimal("123.00"), item);
        item.addBid(someBid);
        item.addBid(someBid);
        bidRepository.save(someBid);

        Item item2 = itemRepository.findItemWithBids(item.getId());

        assertAll(
                () -> assertEquals(2, item.getBids().size()),
                () -> assertEquals(1, item2.getBids().size())
        );

        Bid bid = new Bid(new BigDecimal("456.00"), item);
        item.addBid(bid); // No SELECT!
        bidRepository.save(bid);

        Item item3 = itemRepository.findItemWithBids(item.getId());

        assertEquals(2, item3.getBids().size());
    }
}
