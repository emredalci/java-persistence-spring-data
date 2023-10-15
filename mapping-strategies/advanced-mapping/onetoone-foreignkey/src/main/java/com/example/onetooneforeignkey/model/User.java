
package com.example.onetooneforeignkey.model;


import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false, // NOT NULL
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(unique = true) // Defaults to SHIPPINGADDRESS_ID
    //If you’re more familiar with SQL than JPA, it
    //helps to think “foreign key column” every time you see @JoinColumn in a mapping.
    private Address shippingAddress;

    public User() {
    }

    public User(String username) {
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
