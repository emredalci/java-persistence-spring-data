package com.example.mappingcollections.setofstrings;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.setofstrings.model.SetOfStringsItem;
import com.example.mappingcollections.setofstrings.repository.SetOfStringsItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SetOfStringsTest extends AbstractTestContainer {

    @Autowired
    private SetOfStringsItemRepository setOfStringsItemRepository;

    @Test
    void storeLoadEntities(){
        SetOfStringsItem setOfStringsItem = new SetOfStringsItem("Foo");

        setOfStringsItem.addImage("background.jpg");
        setOfStringsItem.addImage("foreground.jpg");
        setOfStringsItem.addImage("landscape.jpg");
        setOfStringsItem.addImage("portrait.jpg");

        setOfStringsItemRepository.save(setOfStringsItem);

        SetOfStringsItem setOfStringsItemById = setOfStringsItemRepository.findItemWithImages(setOfStringsItem.getId());

        List<SetOfStringsItem> allSetOfStringsItems = setOfStringsItemRepository.findAll();
        Set<String> images = setOfStringsItemRepository.findImagesNative(setOfStringsItem.getId());

        assertAll(
                () -> assertEquals(4, setOfStringsItemById.getImagesSet().size()),
                () -> assertEquals(1, allSetOfStringsItems.size()),
                () -> assertEquals(4, images.size())
        );
    }
}
