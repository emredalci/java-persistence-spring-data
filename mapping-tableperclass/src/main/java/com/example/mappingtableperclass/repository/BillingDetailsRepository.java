package com.example.mappingtableperclass.repository;

import com.example.mappingtableperclass.model.BillingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingDetailsRepository<T extends BillingDetails, I> extends JpaRepository<T,I> {

    List<T> findByOwner(String owner);
}
