package com.example.mappingcollections.setofstrings;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.setofstrings.model.Item;
import com.example.mappingcollections.setofstrings.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SetOfStringsTest extends AbstractTestContainer {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void storeLoadEntities(){
        Item item = new Item("Foo");

        item.addImage("background.jpg");
        item.addImage("foreground.jpg");
        item.addImage("landscape.jpg");
        item.addImage("portrait.jpg");

        itemRepository.save(item);

        Item itemById = itemRepository.findItemWithImages(item.getId());

        List<Item> allItems = itemRepository.findAll();
        Set<String> images = itemRepository.findImagesNative(item.getId());

        assertAll(
                () -> assertEquals(4, itemById.getImages().size()),
                () -> assertEquals(1, allItems.size()),
                () -> assertEquals(4, images.size())
        );
    }
}
