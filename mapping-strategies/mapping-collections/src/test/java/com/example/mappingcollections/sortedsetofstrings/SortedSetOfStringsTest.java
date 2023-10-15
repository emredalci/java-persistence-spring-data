package com.example.mappingcollections.sortedsetofstrings;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.sortedsetofstrings.model.SortedSetOfStringsItem;
import com.example.mappingcollections.sortedsetofstrings.repository.SortedSetOfStringsItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SortedSetOfStringsTest extends AbstractTestContainer {

    @Autowired
    SortedSetOfStringsItemRepository repository;

    @Test
    void test() {
        SortedSetOfStringsItem entity = new SortedSetOfStringsItem("Foo");

        entity.addImage("background.jpg");
        entity.addImage("foreground.jpg");
        entity.addImage("landscape.jpg");
        entity.addImage("portrait.jpg");

        repository.save(entity);

        SortedSetOfStringsItem entityById = repository.findItemWithImages(entity.getId());

        List<SortedSetOfStringsItem> entities = repository.findAll();
        Set<String> images = repository.findImagesNative(entity.getId());

        assertAll(
                () -> assertEquals(4, entityById.getImages().size()),
                () -> assertEquals(1, entities.size()),
                () -> assertEquals(4, images.size())
        );
    }
}
