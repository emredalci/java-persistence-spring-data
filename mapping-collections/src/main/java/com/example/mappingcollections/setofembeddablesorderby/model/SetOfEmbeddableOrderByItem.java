package com.example.mappingcollections.setofembeddablesorderby.model;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class SetOfEmbeddableOrderByItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "IMAGE_SETOFEMBEDDABLEORDERBY")
    @OrderBy("filename DESC, width DESC")
    @AttributeOverride(
            name = "filename",
            column = @Column(name = "FILENAME_SET_EMBEDDABLE_ORDERBY", nullable = false)
    )
    private Set<SetOfembeddableOrderByImage> images = new LinkedHashSet<>();

    public SetOfEmbeddableOrderByItem() {
    }

    public SetOfEmbeddableOrderByItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<SetOfembeddableOrderByImage> getImages() {
        return images;
    }

    public void addImage(SetOfembeddableOrderByImage image) {
        images.add(image);
    }
}
