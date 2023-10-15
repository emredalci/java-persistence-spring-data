package com.example.mappingmappedsuperclass;

import com.example.mappingmappedsuperclass.model.BankAccount;
import com.example.mappingmappedsuperclass.model.CreditCard;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MappedSuperClassTest extends AbstractTestContainer {

    @Test
    void storeLoadEntities() {

        CreditCard creditCard = new CreditCard("John Smith", "123456789", "10", "2030");
        creditCardRepository.save(creditCard);

        BankAccount bankAccount = new BankAccount("Mike Johnson", "12345", "Delta Bank", "BANKXY12");
        bankAccountRepository.save(bankAccount);

        List<CreditCard> creditCards = creditCardRepository.findByOwner("John Smith");
        List<BankAccount> bankAccounts = bankAccountRepository.findByOwner("Mike Johnson");
        List<CreditCard> creditCards2 = creditCardRepository.findByExpYear("2030");
        List<BankAccount> bankAccounts2 = bankAccountRepository.findBySwift("BANKXY12");

        assertAll(
                () -> assertEquals(1, creditCards.size()),
                () -> assertEquals("123456789", creditCards.get(0).getCardNumber()),
                () -> assertEquals(1, bankAccounts.size()),
                () -> assertEquals("12345", bankAccounts.get(0).getAccount()),
                () -> assertEquals(1, creditCards2.size()),
                () -> assertEquals("John Smith", creditCards2.get(0).getOwner()),
                () -> assertEquals(1, bankAccounts2.size()),
                () -> assertEquals("Mike Johnson", bankAccounts2.get(0).getOwner())
        );
    }
}
