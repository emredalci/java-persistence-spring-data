package com.example.mappingcollections.mapofstrings.model;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
public class MapOfStringsItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "IMAGE_MAP")
    @MapKeyColumn(name = "FILENAME_KEY")
    @Column(name = "IMAGENAME_VALUE")
    private Map<String, String> images = new HashMap<>();

    public MapOfStringsItem() {
    }

    public MapOfStringsItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void putImage(String key, String value) {
        images.put(key,value);
    }
}
