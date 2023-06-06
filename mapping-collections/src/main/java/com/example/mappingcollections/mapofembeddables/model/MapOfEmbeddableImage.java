package com.example.mappingcollections.mapofembeddables.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class MapOfEmbeddableImage {

    @Column(nullable = true) // Can be null, not part of PK!
    private String title;

    private int width;

    private int height;

    public MapOfEmbeddableImage() {
    }

    public MapOfEmbeddableImage(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

        MapOfEmbeddableImage that = (MapOfEmbeddableImage) o;

        if (width != that.width) return false;
        if (height != that.height) return false;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}
