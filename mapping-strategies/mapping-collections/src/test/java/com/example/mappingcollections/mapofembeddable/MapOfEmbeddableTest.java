package com.example.mappingcollections.mapofembeddable;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.mapofembeddables.model.MapOfEmbeddableFilename;
import com.example.mappingcollections.mapofembeddables.model.MapOfEmbeddableImage;
import com.example.mappingcollections.mapofembeddables.model.MapOfEmbeddableItem;
import com.example.mappingcollections.mapofembeddables.repository.MapOfEmbeddableItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MapOfEmbeddableTest extends AbstractTestContainer {

    @Autowired
    private MapOfEmbeddableItemRepository repository;

    @Test
    void test() {
        MapOfEmbeddableItem item = new MapOfEmbeddableItem("Foo");

        item.putImage(new MapOfEmbeddableFilename("background.jpg"), new MapOfEmbeddableImage("Background", 640, 480));
        item.putImage(new MapOfEmbeddableFilename("foreground.jpg"), new MapOfEmbeddableImage("Foreground", 640, 480));
        item.putImage(new MapOfEmbeddableFilename("landscape.jpg"), new MapOfEmbeddableImage("Landscape", 640, 480));
        item.putImage(new MapOfEmbeddableFilename("portrait.jpg"), new MapOfEmbeddableImage("Portrait", 480, 640));

        repository.save(item);

        MapOfEmbeddableItem item2 = repository.findItemWithImages(item.getId());

        List<MapOfEmbeddableItem> items2 = repository.findAll();
        Set<String> images = repository.findImagesNative(item.getId());

        assertAll(
                () -> assertEquals(4, item2.getImages().size()),
                () -> assertEquals(1, items2.size()),
                () -> assertEquals(4, images.size())
        );
    }

}
