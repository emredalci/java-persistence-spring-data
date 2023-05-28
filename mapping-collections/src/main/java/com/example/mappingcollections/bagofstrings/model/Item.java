package com.example.mappingcollections.bagofstrings.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity(name = "BagOfStringsItem")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "IMAGE") // Defaults to ITEM_IMAGES
    @Column(name = "FILE_NAME") // Defaults to IMAGES
    @GenericGenerator(name = "sequence_gen")
    @CollectionId(
            column = @Column(name = "IMAGE_ID"),
            generator = "sequence_gen" //TODO Unable to determine JavaType to use : BasicValue([Column(image_id)])
    )
    private Collection<String> images = new ArrayList<>();

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

    public Collection<String> getImages() {
        return Collections.unmodifiableCollection(images);
    }

    public void addImage(String imageName) {
        images.add(imageName);
    }
}
