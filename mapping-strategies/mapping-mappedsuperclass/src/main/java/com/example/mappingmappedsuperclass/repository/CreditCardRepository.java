package com.example.mappingmappedsuperclass.repository;

import com.example.mappingmappedsuperclass.model.CreditCard;

import java.util.List;


public interface CreditCardRepository extends BillingDetailsRepository<CreditCard> {

    List<CreditCard> findByExpYear(String expYear);
}
