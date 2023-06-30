package com.example.manytomanyternary.repository;

import com.example.manytomanyternary.model.Category;
import com.example.manytomanyternary.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c join c.categorizedItems ci where ci.item = :itemParameter")
    List<Category> findCategoryWithCategorizedItems(@Param("itemParameter") Item itemParameter);
}
