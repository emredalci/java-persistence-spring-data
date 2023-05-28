package com.example.mappingcollections.bagofstrings.repository;

import com.example.mappingcollections.bagofstrings.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository(value = "BagOfStringsItemRepository")
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i inner join fetch i.images where i.id = :id")
    Item findItemWithImages(@Param("id") Long id);

    @Query(value = "SELECT FILE_NAME FROM IMAGE WHERE ITEM_ID = ?1", nativeQuery = true)
    Set<String> findImagesNative(Long id);
}
