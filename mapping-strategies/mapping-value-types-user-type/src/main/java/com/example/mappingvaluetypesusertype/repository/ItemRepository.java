package com.example.mappingvaluetypesusertype.repository;

import com.example.mappingvaluetypesusertype.model.Item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
