package com.example.onetomanyembeddablejointable.repository;


import com.example.onetomanyembeddablejointable.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
