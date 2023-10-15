package com.example.mappingcollections.setofembeddablesorderby.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Parent;

import java.util.Objects;

@Embeddable
public class SetOfembeddableOrderByImage {

    @Column(nullable = false)
    private String filename;

    private int width;

    private int height;

    @Parent
    private SetOfEmbeddableOrderByItem item;

    public SetOfembeddableOrderByImage() {
    }

    public SetOfembeddableOrderByImage(String filename, int width, int height) {
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

    public SetOfEmbeddableOrderByItem getItem() {
        return item;
    }

    public void setItem(SetOfEmbeddableOrderByItem item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetOfembeddableOrderByImage that = (SetOfembeddableOrderByImage) o;

        if (width != that.width) return false;
        if (height != that.height) return false;
        if (!Objects.equals(filename, that.filename)) return false;
        return Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + (item != null ? item.hashCode() : 0);
        return result;
    }
}
