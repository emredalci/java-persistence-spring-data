package com.example.mappingmappedsuperclass.repository;

import com.example.mappingmappedsuperclass.model.BankAccount;

import java.util.List;

public interface BankAccountRepository extends BillingDetailsRepository<BankAccount> {

    List<BankAccount> findBySwift(String swift);
}
