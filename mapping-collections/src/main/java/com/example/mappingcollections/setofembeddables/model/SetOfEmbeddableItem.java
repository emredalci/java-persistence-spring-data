package com.example.mappingcollections.setofembeddables.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SetOfEmbeddableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "IMAGE_SET_EMBEDDABLE")
    @AttributeOverride(
            name = "filename",
            column = @Column(name = "FILENAME_SET_EMBEDDABLE", nullable = false)
    )
    private Set<SetOfEmbeddableImage> images = new HashSet<>();

    public SetOfEmbeddableItem() {
    }

    public SetOfEmbeddableItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<SetOfEmbeddableImage> getImages() {
        return Collections.unmodifiableSet(images);
    }

    public void addImage(SetOfEmbeddableImage image) {
        images.add(image);
    }
}
