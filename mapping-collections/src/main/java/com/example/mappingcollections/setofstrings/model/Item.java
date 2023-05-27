package com.example.mappingcollections.setofstrings.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(
            name = "IMAGE", // Defaults to ITEM_IMAGES
            joinColumns = @JoinColumn(name = "ITEM_ID") // Defaults to ITEM_ID
    )
    @Column(name = "FILE_NAME") // Defaults to IMAGES
    private Set<String> images = new HashSet<>();

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getImages() {
        return Collections.unmodifiableSet(images);
    }

    public void addImage(String imageName) {
        images.add(imageName);
    }
}
