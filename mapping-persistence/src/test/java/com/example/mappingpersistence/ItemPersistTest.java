package com.example.mappingpersistence;

import com.example.mappingpersistence.model.Item;
import com.example.mappingpersistence.repository.ItemRepository;
import com.example.mappingpersistence.utils.Helper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemPersistTest extends AbstractTestContainer{

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void storeLoadItem() {

        Item item = new Item();
        item.setName("Some Item");
        item.setAuctionEnd(Helper.tomorrow());

        itemRepository.save(item);

        List<Item> items = itemRepository.findAll();

        assertAll(
                () -> assertEquals(1000, items.get(0).getId()),
                () -> assertEquals(1, items.size()),
                () -> assertEquals("Some Item", items.get(0).getName())
        );

    }
}
