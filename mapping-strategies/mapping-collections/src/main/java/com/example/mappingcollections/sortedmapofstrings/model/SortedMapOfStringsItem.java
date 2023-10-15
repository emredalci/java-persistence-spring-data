package com.example.mappingcollections.sortedmapofstrings.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SortComparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

@Entity
public class SortedMapOfStringsItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "IMAGE_SORTED_MAP")
    @MapKeyColumn(name = "FILENAME_SORTED_KEY")
    @Column(name = "IMAGENAME_SORTED_VALUE")
    @SortComparator(ReverseStringComparator.class)
    private SortedMap<String, String> images = new TreeMap<>();

    public SortedMapOfStringsItem() {
    }

    public SortedMapOfStringsItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SortedMap<String, String> getImages() {
        return Collections.unmodifiableSortedMap(images);
    }

    public void putImage(String key, String value) {
        images.put(key,value);
    }

    public static class ReverseStringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o2.compareTo(o1);
        }
    }
}
