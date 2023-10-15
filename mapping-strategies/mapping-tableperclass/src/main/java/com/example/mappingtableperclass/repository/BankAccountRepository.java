package com.example.mappingtableperclass.repository;

import com.example.mappingtableperclass.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findBySwift(String swift);

}
