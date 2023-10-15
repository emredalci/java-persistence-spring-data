package com.example.mappingvaluetypes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Address homeAddress;

    @AttributeOverride(name = "street", column = @Column(name = "BILLING_STREET")) //!NULLABLE
    @AttributeOverride(name = "zipcode", column = @Column(name = "BILLING_ZIPCODE"))
    @AttributeOverride(name = "city", column = @Column(name = "BILLING_CITY"))
    private Address billingAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }
}
