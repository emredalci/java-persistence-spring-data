package com.example.mappingassociations.ondeletecascade;

import com.example.mappingassociations.MappingAssociationsApplication;
import com.example.mappingassociations.onetomany.ondeletecascade.Bid;
import com.example.mappingassociations.onetomany.ondeletecascade.Item;
import com.example.mappingassociations.repositories.ondeletecascade.BidOnDeleteRepository;
import com.example.mappingassociations.repositories.ondeletecascade.ItemOnDeleteRepository;
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
class OnDeleteTest {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    private ItemOnDeleteRepository itemRepository;

    @Autowired
    private BidOnDeleteRepository bidRepository;

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

        Item item1 = itemRepository.findById(item.getId()).get();

        itemRepository.delete(item1);

        List<Item> items2 = itemRepository.findAll();
        List<Bid> bids2 = bidRepository.findAll();

        assertAll(
                () -> assertEquals(0, items2.size()),
                () -> assertEquals(0, bids2.size())
        );
    }


}
