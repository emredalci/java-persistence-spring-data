package com.example.mappingcollections.sortedmapofstrings.repository;

import com.example.mappingcollections.sortedmapofstrings.model.SortedMapOfStringsItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface SortedMapOfStringsItemRepository extends JpaRepository<SortedMapOfStringsItem, Long> {

    @Query("select i from SortedMapOfStringsItem i inner join fetch i.images where i.id = :id")
    SortedMapOfStringsItem findItemWithImages(@Param("id") Long id);

    @Query(value = "SELECT FILENAME_SORTED_KEY FROM IMAGE_SORTED_MAP WHERE sorted_map_of_strings_item_id = ?1", nativeQuery = true)
    Set<String> findImagesNative(Long id);

}
