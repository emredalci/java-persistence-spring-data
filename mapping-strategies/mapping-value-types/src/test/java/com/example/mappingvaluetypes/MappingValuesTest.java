package com.example.mappingvaluetypes;

import com.example.mappingvaluetypes.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MappingValuesTest extends AbstractTestContainer{

    @Test
    void storeLoadEntities() {

        User user = new User();
        user.setUsername("username");
        user.setHomeAddress(new Address("Flowers Street", "12345", "Boston"));
        userRepository.save(user);

        Item item = new Item();
        item.setName("Some Item");
        item.setMetricWeight(2.0);
        item.setBuyNowPrice(new MonetaryAmount(BigDecimal.valueOf(1.1), Currency.getInstance("USD")));
        item.setDescription("descriptiondescription");
        itemRepository.save(item);

        List<User> users = userRepository.findAll();
        List<Item> items = itemRepository.findByMetricWeight(2.0);

        assertAll(
                () -> assertEquals(1, users.size()),
                () -> assertEquals("username", users.get(0).getUsername()),
                () -> assertEquals("Flowers Street", users.get(0).getHomeAddress().getStreet()),
                () -> assertEquals("12345", users.get(0).getHomeAddress().getZipcode()),
                () -> assertEquals("Boston", users.get(0).getHomeAddress().getCity()),
                () -> assertEquals(1, items.size()),
                () -> assertEquals("AUCTION: Some Item", items.get(0).getName()),
                () -> assertEquals("1.1 USD", items.get(0).getBuyNowPrice().toString()),
                () -> assertEquals("descriptiondescription", items.get(0).getDescription()),
                () -> assertEquals(AuctionType.HIGHEST_BID, items.get(0).getAuctionType()),
                () -> assertEquals("descriptiond...", items.get(0).getShortDescription()),
                () -> assertEquals(2.0, items.get(0).getMetricWeight()),
                () -> assertEquals(LocalDate.now(), items.get(0).getCreatedOn()),
                () -> assertTrue(ChronoUnit.SECONDS.between(LocalDateTime.now(), items.get(0).getLastModified()) < 1),
                () -> assertEquals(new BigDecimal("1.00"), items.get(0).getInitialPrice())
        );

    }
}
