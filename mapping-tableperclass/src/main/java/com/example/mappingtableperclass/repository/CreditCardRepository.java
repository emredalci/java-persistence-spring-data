package com.example.mappingtableperclass.repository;

import com.example.mappingtableperclass.model.CreditCard;

import java.util.List;

public interface CreditCardRepository extends BillingDetailsRepository<CreditCard, Long>{
    List<CreditCard> findByExpYear(String expYear);
}
