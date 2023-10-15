
package com.example.onetomanybag.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "item")
    private Collection<Bid> bids = new ArrayList<>();

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Bid> getBids() {
        return Collections.unmodifiableCollection(bids);
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }
}
