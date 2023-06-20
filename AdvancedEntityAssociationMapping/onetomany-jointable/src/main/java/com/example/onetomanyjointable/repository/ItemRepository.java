package com.example.onetomanyjointable.repository;


import com.example.onetomanyjointable.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
