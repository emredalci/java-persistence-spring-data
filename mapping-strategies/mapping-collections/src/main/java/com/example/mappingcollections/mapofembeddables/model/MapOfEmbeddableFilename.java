package com.example.mappingcollections.mapofembeddables.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class MapOfEmbeddableFilename {

    @Column(nullable = false)
    private String name;

    public MapOfEmbeddableFilename() {
    }

    public MapOfEmbeddableFilename(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapOfEmbeddableFilename that = (MapOfEmbeddableFilename) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
