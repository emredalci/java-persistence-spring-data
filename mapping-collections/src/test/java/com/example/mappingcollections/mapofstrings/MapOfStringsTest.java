package com.example.mappingcollections.mapofstrings;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.mapofstrings.model.MapOfStringsItem;
import com.example.mappingcollections.mapofstrings.repository.MapOfStringsItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MapOfStringsTest extends AbstractTestContainer {

    @Autowired
    MapOfStringsItemRepository repository;

    @Test
    void test(){
        MapOfStringsItem entity = new MapOfStringsItem("Foo");

        entity.putImage("Background", "background.jpg");
        entity.putImage("Foreground", "foreground.jpg");
        entity.putImage("Landscape", "landscape.jpg");
        entity.putImage("Portrait", "portrait.jpg");

        repository.save(entity);

        MapOfStringsItem entityById = repository.findItemWithImages(entity.getId());

        List<MapOfStringsItem> entities = repository.findAll();
        Set<String> images = repository.findImagesNative(entity.getId());

        assertAll(
                () -> assertEquals(4, entityById.getImages().size()),
                () -> assertEquals(1, entities.size()),
                () -> assertEquals(4, images.size())
        );
    }

}
