package com.example.mappingcollections.setofembeddablesorderby.repository;

import com.example.mappingcollections.setofembeddablesorderby.model.SetOfEmbeddableOrderByItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface SetOfEmbeddableOrderByItemRepository extends JpaRepository<SetOfEmbeddableOrderByItem, Long> {

    @Query("select i from SetOfEmbeddableOrderByItem i inner join fetch i.images where i.id = :id")
    SetOfEmbeddableOrderByItem findItemWithImages(@Param("id") Long id);

    @Query(value = "SELECT FILENAME_SET_EMBEDDABLE_ORDERBY FROM IMAGE_SETOFEMBEDDABLEORDERBY WHERE set_of_embeddable_order_by_item_id = ?1", nativeQuery = true)
    Set<String> findImagesNative(Long id);

}
