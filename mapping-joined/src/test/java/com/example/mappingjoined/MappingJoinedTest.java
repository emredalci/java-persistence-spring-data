package com.example.mappingjoined;

import com.example.mappingjoined.model.BankAccount;
import com.example.mappingjoined.model.BillingDetails;
import com.example.mappingjoined.model.CreditCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MappingJoinedTest extends AbstractTestContainer {

    @Test
    void storeLoadTest() {

        CreditCard creditCard = new CreditCard("John Smith", "123456789", "10", "2030");
        billingDetailsRepository.save(creditCard);

        BankAccount bankAccount = new BankAccount("Mike Johnson", "12345", "Delta Bank", "BANKXY12");
        billingDetailsRepository.save(bankAccount);

        List<BillingDetails> billingDetails = billingDetailsRepository.findAll();
        List<BillingDetails> billingDetails2 = billingDetailsRepository.findByOwner("John Smith");
        List<CreditCard> creditCards = creditCardRepository.findAll();

        assertAll(
                () -> assertEquals(2, billingDetails.size()),
                () -> assertEquals(1, billingDetails2.size()),
                () -> assertEquals("John Smith", billingDetails2.get(0).getOwner()),
                () -> assertEquals(1, creditCards.size())
        );
    }

}
