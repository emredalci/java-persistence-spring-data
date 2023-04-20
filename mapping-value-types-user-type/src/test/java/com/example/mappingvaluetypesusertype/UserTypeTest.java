package com.example.mappingvaluetypesusertype;

import com.example.mappingvaluetypesusertype.model.Item;
import com.example.mappingvaluetypesusertype.model.MonetaryAmount;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTypeTest extends AbstractTestContainer {

    @Test
    void storeLoadEntity() {
        Item item = new Item();
        item.setInitialPrice(new MonetaryAmount(new BigDecimal("1.00"), Currency.getInstance("USD")));
        item.setBuyNowPrice(new MonetaryAmount(new BigDecimal("1.10"), Currency.getInstance("USD")));

        itemRepository.save(item);

        List<Item> items = itemRepository.findAll();

        assertAll(
                () -> assertEquals("2.20 USD", items.get(0).getBuyNowPrice().toString()),
                () -> assertEquals("2.00 EUR", items.get(0).getInitialPrice().toString())
        );


    }

}
