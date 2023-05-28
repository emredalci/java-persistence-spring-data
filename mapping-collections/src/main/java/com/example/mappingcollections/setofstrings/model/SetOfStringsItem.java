package com.example.mappingcollections.setofstrings.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SetOfStringsItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(
            name = "IMAGE_SET", // Defaults to ITEM_IMAGES
            joinColumns = @JoinColumn(name = "SETOFSTRINGS_ID") // Defaults to ITEM_ID
    )
    @Column(name = "FILENAME_SET") // Defaults to IMAGES
    private Set<String> imagesSet = new HashSet<>();

    public SetOfStringsItem() {
    }

    public SetOfStringsItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getImagesSet() {
        return Collections.unmodifiableSet(imagesSet);
    }

    public void addImage(String imageName) {
        imagesSet.add(imageName);
    }
}
