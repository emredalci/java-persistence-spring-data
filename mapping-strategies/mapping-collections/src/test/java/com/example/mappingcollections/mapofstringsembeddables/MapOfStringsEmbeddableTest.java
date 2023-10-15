package com.example.mappingcollections.mapofstringsembeddables;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.mapofstringembeddables.model.MapOfStringEmbeddableImage;
import com.example.mappingcollections.mapofstringembeddables.model.MapOfStringEmbeddableItem;
import com.example.mappingcollections.mapofstringembeddables.repository.MapOfStringEmbeddableItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MapOfStringsEmbeddableTest extends AbstractTestContainer {

    @Autowired
    private MapOfStringEmbeddableItemRepository repository;

    @Test
    void test() {
        MapOfStringEmbeddableItem item = new MapOfStringEmbeddableItem("Foo");

        item.putImage("Background", new MapOfStringEmbeddableImage("background.jpg", 640, 480));
        item.putImage("Foreground", new MapOfStringEmbeddableImage("foreground.jpg", 640, 480));
        item.putImage("Landscape", new MapOfStringEmbeddableImage("landscape.jpg", 640, 480));
        item.putImage("Portrait", new MapOfStringEmbeddableImage("portrait.jpg", 480, 640));

        repository.save(item);

        MapOfStringEmbeddableItem item2 = repository.findItemWithImages(item.getId());

        List<MapOfStringEmbeddableItem> items2 = repository.findAll();
        Set<String> images = repository.findImagesNative(item.getId());

        assertAll(
                () -> assertEquals(4, item2.getImages().size()),
                () -> assertEquals(1, items2.size()),
                () -> assertEquals(4, images.size())
        );
    }
}
