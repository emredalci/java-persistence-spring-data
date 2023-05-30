package com.example.mappingcollections.mapofstrings.repository;

import com.example.mappingcollections.mapofstrings.model.MapOfStringsItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MapOfStringsItemRepository extends JpaRepository<MapOfStringsItem, Long> {

    @Query("select i from MapOfStringsItem i inner join fetch i.images where i.id = :id")
    MapOfStringsItem findItemWithImages(@Param("id") Long id);

    @Query(value = "SELECT FILENAME_KEY FROM IMAGE_MAP WHERE map_of_strings_item_id = ?1", nativeQuery = true)
    Set<String> findImagesNative(Long id);

}
