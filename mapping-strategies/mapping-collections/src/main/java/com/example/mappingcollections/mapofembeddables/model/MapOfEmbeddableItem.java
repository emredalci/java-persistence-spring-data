package com.example.mappingcollections.mapofembeddables.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
public class MapOfEmbeddableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "IMAGE_MAPOFEMBEDDABLE")
    private Map<MapOfEmbeddableFilename, MapOfEmbeddableImage> images = new HashMap<>();

    public MapOfEmbeddableItem() {
    }

    public MapOfEmbeddableItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<MapOfEmbeddableFilename, MapOfEmbeddableImage> getImages() {
        return Collections.unmodifiableMap(images);
    }

    public void putImage(MapOfEmbeddableFilename filename, MapOfEmbeddableImage image) {
        images.put(filename, image);
    }
}
