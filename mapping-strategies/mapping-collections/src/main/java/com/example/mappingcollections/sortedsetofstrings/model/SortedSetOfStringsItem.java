package com.example.mappingcollections.sortedsetofstrings.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SortNatural;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class SortedSetOfStringsItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "IMAGE_SORTED_LIST")
    @Column(name = "FILENAME_SORTED_LIST")
    @SortNatural
    private SortedSet<String> images = new TreeSet<>();

    public SortedSetOfStringsItem() {
    }

    public SortedSetOfStringsItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SortedSet<String> getImages() {
        return Collections.unmodifiableSortedSet(images);
    }

    public void addImage(String image) {
        images.add(image);
    }
}
