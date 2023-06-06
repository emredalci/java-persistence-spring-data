package com.example.mappingcollections.mapofstringembeddables.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class MapOfStringEmbeddableImage {

    @Column(nullable = true) // Can be null, not part of PK!
    private String filename;

    private int width;

    private int height;

    public MapOfStringEmbeddableImage() {
    }

    public MapOfStringEmbeddableImage(String filename, int width, int height) {
        this.filename = filename;
        this.width = width;
        this.height = height;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Whenever value-types are managed in collections, overriding equals/hashCode is a good idea!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapOfStringEmbeddableImage that = (MapOfStringEmbeddableImage) o;

        if (width != that.width) return false;
        if (height != that.height) return false;
        return Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}
