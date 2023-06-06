package com.example.mappingcollections.mapofstringembeddables.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
public class MapOfStringEmbeddableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "IMAGE_MAPOFSTRINGEMBEDDABLE")
    @MapKeyColumn(name = "IMAGE_MAPOFSTRINGEMBEDDABLE_KEY")
    private Map<String, MapOfStringEmbeddableImage> images = new HashMap<>();

    public MapOfStringEmbeddableItem() {
    }

    public MapOfStringEmbeddableItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, MapOfStringEmbeddableImage> getImages() {
        return Collections.unmodifiableMap(images);
    }

    public void putImage(String key, MapOfStringEmbeddableImage image) {
        images.put(key, image);
    }
}
