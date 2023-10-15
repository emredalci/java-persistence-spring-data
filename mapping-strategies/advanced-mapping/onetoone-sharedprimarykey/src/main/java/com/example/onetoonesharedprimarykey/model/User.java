
package com.example.onetoonesharedprimarykey.model;


import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    private Long id;

    private String username;

    @OneToOne(
            fetch = FetchType.LAZY,  // Defaults to EAGER
            optional = false, // Required for lazy loading with proxies!
            cascade = CascadeType.ALL // Any change here must be cascaded to Address
    )
    @PrimaryKeyJoinColumn //makes this a unidirectional shared primary key one-to-one association mapping,
    private Address shippingAddress;

    public User() {
    }

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
