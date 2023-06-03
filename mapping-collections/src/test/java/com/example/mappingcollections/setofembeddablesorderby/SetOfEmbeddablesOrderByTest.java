package com.example.mappingcollections.setofembeddablesorderby;

import com.example.mappingcollections.AbstractTestContainer;
import com.example.mappingcollections.setofembeddablesorderby.model.SetOfEmbeddableOrderByItem;
import com.example.mappingcollections.setofembeddablesorderby.model.SetOfembeddableOrderByImage;
import com.example.mappingcollections.setofembeddablesorderby.repository.SetOfEmbeddableOrderByItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SetOfEmbeddablesOrderByTest extends AbstractTestContainer {

    @Autowired
    SetOfEmbeddableOrderByItemRepository repository;

    @Test
    void test() {

        SetOfEmbeddableOrderByItem item = new SetOfEmbeddableOrderByItem("Foo");

        item.addImage(new SetOfembeddableOrderByImage("background.jpg", 640, 480));
        item.addImage(new SetOfembeddableOrderByImage("foreground.jpg", 640, 480));
        item.addImage(new SetOfembeddableOrderByImage("landscape.jpg", 640, 480));
        item.addImage(new SetOfembeddableOrderByImage("portrait.jpg", 480, 640));

        repository.save(item);

        SetOfEmbeddableOrderByItem item2 = repository.findItemWithImages(item.getId());

        List<SetOfEmbeddableOrderByItem> items2 = repository.findAll();
        Set<String> images = repository.findImagesNative(item.getId());

        assertAll(
                () -> assertEquals(4, item2.getImages().size()),
                () -> assertEquals(1, items2.size()),
                () -> assertEquals(4, images.size()),
                () -> assertEquals("portrait.jpg", new ArrayList<>(item2.getImages()).get(0).getFilename()),
                () -> assertEquals("background.jpg", new ArrayList<>(item2.getImages()).get(item2.getImages().size() - 1).getFilename())
        );

    }
}
