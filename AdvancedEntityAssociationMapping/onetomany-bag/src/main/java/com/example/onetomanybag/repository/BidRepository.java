
package com.example.onetomanybag.repository;


import com.example.onetomanybag.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}
