package com.example.mappingpersistence.repository;

import com.example.mappingpersistence.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
