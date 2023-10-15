package com.example.manytomanylinkentity.repository;

import com.example.manytomanylinkentity.model.CategorizedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorizedItemRepository extends JpaRepository<CategorizedItem, CategorizedItem.Id> {
}
