package com.example.mappingassociations.repositories.bidirectional;

import com.example.mappingassociations.onetomany.bidirectional.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long> {
}
