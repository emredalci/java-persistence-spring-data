package com.example.mappingmappedsuperclass.repository;

import com.example.mappingmappedsuperclass.model.BillingDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BillingDetailsRepository<T extends BillingDetails> extends JpaRepository<T, Long> {

    List<T> findByOwner(String owner);
}
