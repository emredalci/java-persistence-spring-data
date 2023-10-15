package com.example.mappingcollections.setofembeddables.repository;

import com.example.mappingcollections.setofembeddables.model.SetOfEmbeddableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface SetOfEmbeddableItemRepository extends JpaRepository<SetOfEmbeddableItem, Long> {

    @Query("select i from SetOfEmbeddableItem i inner join fetch i.images where i.id = :id")
    SetOfEmbeddableItem findItemWithImages(@Param("id") Long id);

    @Query(value = "SELECT FILENAME_SET_EMBEDDABLE FROM IMAGE_SET_EMBEDDABLE WHERE set_of_embeddable_item_id = ?1",
            nativeQuery = true)
    Set<String> findImagesNative(Long id);
}
