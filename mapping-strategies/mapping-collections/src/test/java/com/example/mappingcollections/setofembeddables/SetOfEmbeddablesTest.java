package com.example.mappingcollections.setofembeddables;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.setofembeddables.model.SetOfEmbeddableImage;
import com.example.mappingcollections.setofembeddables.model.SetOfEmbeddableItem;
import com.example.mappingcollections.setofembeddables.repository.SetOfEmbeddableItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SetOfEmbeddablesTest extends AbstractTestContainer {

    @Autowired
    private SetOfEmbeddableItemRepository repository;

    @Test
    void test() {

        SetOfEmbeddableItem item = new SetOfEmbeddableItem("Foo");

        item.addImage(new SetOfEmbeddableImage("background.jpg", 640, 480));
        item.addImage(new SetOfEmbeddableImage("foreground.jpg", 640, 480));
        item.addImage(new SetOfEmbeddableImage("landscape.jpg", 640, 480));
        item.addImage(new SetOfEmbeddableImage("portrait.jpg", 480, 640));

        repository.save(item);

        SetOfEmbeddableItem itemById = repository.findItemWithImages(item.getId());
        List<SetOfEmbeddableItem> items = repository.findAll();
        Set<String> images = repository.findImagesNative(item.getId());

        assertAll(
                () -> assertEquals(4, itemById.getImages().size()),
                () -> assertEquals(1, items.size()),
                () -> assertEquals(4, images.size())
        );

    }

}
