package com.example.mappingcollections.setofstrings.repository;

import com.example.mappingcollections.setofstrings.model.SetOfStringsItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface SetOfStringsItemRepository extends JpaRepository<SetOfStringsItem, Long> {

    @Query("select i from SetOfStringsItem i inner join fetch i.imagesSet where i.id = :id")
    SetOfStringsItem findItemWithImages(@Param("id") Long id);

    @Query(value = "SELECT FILENAME_SET FROM IMAGE_SET WHERE SETOFSTRINGS_ID = ?1", nativeQuery = true)
    Set<String> findImagesNative(Long id);
}
