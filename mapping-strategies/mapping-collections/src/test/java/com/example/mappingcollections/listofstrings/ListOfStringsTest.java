package com.example.mappingcollections.listofstrings;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.listofstrings.model.ListOfStringsItem;
import com.example.mappingcollections.listofstrings.repository.ListOfStringsItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ListOfStringsTest extends AbstractTestContainer {

    @Autowired
    ListOfStringsItemRepository listOfStringsItemRepository;

    @Test
    void storeLoadEntities() {
        ListOfStringsItem listOfStringsItem = new ListOfStringsItem("Foo");

        listOfStringsItem.addImage("background.jpg");
        listOfStringsItem.addImage("foreground.jpg");
        listOfStringsItem.addImage("landscape.jpg");
        listOfStringsItem.addImage("portrait.jpg");

        listOfStringsItemRepository.save(listOfStringsItem);

        ListOfStringsItem listOfStringsItemById = listOfStringsItemRepository.findItemWithImages(listOfStringsItem.getId());

        List<ListOfStringsItem> listOfStringsItems = listOfStringsItemRepository.findAll();
        Set<String> images = listOfStringsItemRepository.findImagesNative(listOfStringsItem.getId());

        assertAll(
                () -> assertEquals(4, listOfStringsItemById.getImagesList().size()),
                () -> assertEquals(1, listOfStringsItems.size()),
                () -> assertEquals(4, images.size())
        );
    }
}
