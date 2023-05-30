package com.example.mappingcollections.sortedmapofstrings;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.sortedmapofstrings.model.SortedMapOfStringsItem;
import com.example.mappingcollections.sortedmapofstrings.repository.SortedMapOfStringsItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SortedMapOfStringsTest extends AbstractTestContainer {

    @Autowired
    SortedMapOfStringsItemRepository repository;

    @Test
    void test(){
        SortedMapOfStringsItem entity = new SortedMapOfStringsItem("Foo");

        entity.putImage("Background", "background.jpg");
        entity.putImage("Foreground", "foreground.jpg");
        entity.putImage("Landscape", "landscape.jpg");
        entity.putImage("Portrait", "portrait.jpg");

        repository.save(entity);

        SortedMapOfStringsItem entityById = repository.findItemWithImages(entity.getId());

        List<SortedMapOfStringsItem> entities = repository.findAll();
        Set<String> images = repository.findImagesNative(entity.getId());

        assertAll(
                () -> assertEquals(4, entityById.getImages().size()),
                () -> assertEquals(1, entities.size()),
                () -> assertEquals(4, images.size()),
                () -> assertEquals("Portrait", entityById.getImages().firstKey()),
                () -> assertEquals("Background", entityById.getImages().lastKey())
        );
    }
}
