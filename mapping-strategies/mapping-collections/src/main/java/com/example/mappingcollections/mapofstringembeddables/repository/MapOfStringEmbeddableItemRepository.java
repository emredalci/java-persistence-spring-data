package com.example.mappingcollections.mapofstringembeddables.repository;

import com.example.mappingcollections.mapofstringembeddables.model.MapOfStringEmbeddableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MapOfStringEmbeddableItemRepository extends JpaRepository<MapOfStringEmbeddableItem, Long> {

    @Query("select i from MapOfStringEmbeddableItem i inner join fetch i.images where i.id = :id")
    MapOfStringEmbeddableItem findItemWithImages(@Param("id") Long id);

    @Query(value = "SELECT IMAGE_MAPOFSTRINGEMBEDDABLE_KEY FROM IMAGE_MAPOFSTRINGEMBEDDABLE WHERE map_of_string_embeddable_item_id = ?1", nativeQuery = true)
    Set<String> findImagesNative(Long id);
}
