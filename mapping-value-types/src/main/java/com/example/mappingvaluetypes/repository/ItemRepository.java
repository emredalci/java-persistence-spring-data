package com.example.mappingvaluetypes.repository;

import com.example.mappingvaluetypes.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByMetricWeight(double weight);
}
