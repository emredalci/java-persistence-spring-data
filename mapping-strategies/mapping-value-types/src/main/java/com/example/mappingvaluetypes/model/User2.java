package com.example.mappingvaluetypes.model;

import com.example.mappingvaluetypes.converter.ZipcodeConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "USER2")
public class User2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Convert(
            converter = ZipcodeConverter.class,
            attributeName = "city.zipcode" // Or "city.zipcode" for nested embeddables
    )
    private Address2 homeAddress;

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

    public Address2 getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address2 homeAddress) {
        this.homeAddress = homeAddress;
    }
}
