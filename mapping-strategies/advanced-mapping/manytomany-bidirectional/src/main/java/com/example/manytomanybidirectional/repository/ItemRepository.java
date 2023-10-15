package com.example.manytomanybidirectional.repository;

import com.example.manytomanybidirectional.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
