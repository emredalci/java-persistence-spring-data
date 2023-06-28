package com.example.onetomanyembeddablejointable.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Embeddable
public class Address {

    @NotNull
    @Column(nullable = false)
    private String street;

    @NotNull
    @Column(nullable = false, length = 5)
    private String zipcode;

    @NotNull
    @Column(nullable = false)
    private String city;

    @OneToMany
    @JoinTable(
            name = "DELIVERIES", // Defaults to USERS_SHIPMENT
            joinColumns =
            @JoinColumn(name = "USER_ID"), // Defaults to USERS_ID
            inverseJoinColumns =
            @JoinColumn(name = "SHIPMENT_ID") // Defaults to SHIPMENTS_ID
    )
    private Set<Shipment> deliveries = new HashSet<>();

    public Address() {
    }

    public Address(String street, String zipcode, String city) {
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Shipment> getDeliveries() {
        return Collections.unmodifiableSet(deliveries);
    }

    public void addDelivery(Shipment shipment) {
        deliveries.add(shipment);
    }

}
