package com.example.transactionswithspring.repository;


import com.example.transactionswithspring.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {
}
