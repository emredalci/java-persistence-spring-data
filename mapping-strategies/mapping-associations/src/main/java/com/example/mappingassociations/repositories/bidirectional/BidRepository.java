package com.example.mappingassociations.repositories.bidirectional;

import com.example.mappingassociations.onetomany.bidirectional.Bid;
import com.example.mappingassociations.onetomany.bidirectional.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Set<Bid> findByItem(Item item);
}
