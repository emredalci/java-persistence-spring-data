package com.example.mappingmappedsuperclass.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class BankAccount extends BillingDetails{

    @NotNull
    private String account;

    @NotNull
    private String bankName;

    @NotNull
    private String swift;

    public BankAccount() {
    }

    public BankAccount(String owner, String account, String bankName, String swift) {
        super(owner);
        this.account = account;
        this.bankName = bankName;
        this.swift = swift;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }
}
