package com.example.mappingcollections.mapofembeddables.repository;

import com.example.mappingcollections.mapofembeddables.model.MapOfEmbeddableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MapOfEmbeddableItemRepository extends JpaRepository<MapOfEmbeddableItem, Long> {

    @Query("select i from MapOfEmbeddableItem i inner join fetch i.images where i.id = :id")
    MapOfEmbeddableItem findItemWithImages(@Param("id") Long id);

    @Query(value = "SELECT TITLE FROM IMAGE_MAPOFEMBEDDABLE WHERE map_of_embeddable_item_id = ?1", nativeQuery = true)
    Set<String> findImagesNative(Long id);
}
