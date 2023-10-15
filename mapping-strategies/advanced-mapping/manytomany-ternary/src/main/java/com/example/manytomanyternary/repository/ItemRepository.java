package com.example.manytomanyternary.repository;


import com.example.manytomanyternary.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
