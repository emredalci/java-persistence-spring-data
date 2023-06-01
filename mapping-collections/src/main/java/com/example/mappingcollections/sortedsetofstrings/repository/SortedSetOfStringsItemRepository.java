package com.example.mappingcollections.sortedsetofstrings.repository;

import com.example.mappingcollections.sortedsetofstrings.model.SortedSetOfStringsItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface SortedSetOfStringsItemRepository extends JpaRepository<SortedSetOfStringsItem, Long> {

    @Query("select i from SortedSetOfStringsItem i inner join fetch i.images where i.id = :id")
    SortedSetOfStringsItem findItemWithImages(@Param("id") Long id);

    @Query(value = "SELECT FILENAME_SORTED_LIST FROM IMAGE_SORTED_LIST WHERE sorted_set_of_strings_item_id = ?1", nativeQuery = true)
    Set<String> findImagesNative(Long id);


}
