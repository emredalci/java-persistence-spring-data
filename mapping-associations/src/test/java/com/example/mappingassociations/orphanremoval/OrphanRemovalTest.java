package com.example.mappingassociations.orphanremoval;

import com.example.mappingassociations.MappingAssociationsApplication;
import com.example.mappingassociations.onetomany.orphanremoval.Bid;
import com.example.mappingassociations.onetomany.orphanremoval.Item;
import com.example.mappingassociations.onetomany.orphanremoval.User;
import com.example.mappingassociations.repositories.orphanremoval.BidOrphanRemovalRepository;
import com.example.mappingassociations.repositories.orphanremoval.ItemOrphanRemovalRepository;
import com.example.mappingassociations.repositories.orphanremoval.UserOrphanRemovalRepository;
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
class OrphanRemovalTest {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    private ItemOrphanRemovalRepository itemRepository;

    @Autowired
    private BidOrphanRemovalRepository bidRepository;

    @Autowired
    private UserOrphanRemovalRepository userRepository;

    @Test
    void test() {
        User john = new User("John Smith");
        userRepository.save(john);

        Item item = new Item("Foo");

        Bid bid = new Bid(BigDecimal.valueOf(100), item);
        Bid bid2 = new Bid(BigDecimal.valueOf(200), item);
        item.addBid(bid);
        bid.setBidder(john);
        item.addBid(bid2);
        bid2.setBidder(john);

        itemRepository.save(item);

        List<Item> items = itemRepository.findAll();
        Set<Bid> bids = bidRepository.findByItem(item);
        User user = userRepository.findUserWithBids(john.getId());

        assertAll(
                () -> assertEquals(1, items.size()),
                () -> assertEquals(2, bids.size()),
                () -> assertEquals(2, user.getBids().size())
        );

        Item item1 = itemRepository.findItemWithBids(item.getId());
        Bid firstBid = item1.getBids().iterator().next();
        item1.removeBid(firstBid);

        itemRepository.save(item1);

        List<Item> items2 = itemRepository.findAll();
        List<Bid> bids2 = bidRepository.findAll();


        assertAll(
                () -> assertEquals(1, items2.size()),
                () -> assertEquals(1, bids2.size()),
                () -> assertEquals(2, user.getBids().size())
                //FAILURE
                //() -> assertEquals(1, user.getBids().size())
        );
    }
}
