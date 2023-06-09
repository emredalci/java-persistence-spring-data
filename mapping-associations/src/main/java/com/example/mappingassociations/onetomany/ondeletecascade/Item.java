
package com.example.mappingassociations.onetomany.ondeletecascade;


import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ItemOnDelete")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    // Hibernate quirk: Schema options usually on the 'mappedBy' side
    /*
    All the removal operations we’ve shown so far are inefficient. Bids have to be loaded
    into memory, and many SQL DELETEs are necessary. SQL databases support a more
    efficient foreign key feature: the ON DELETE option. In DDL, it looks like this: foreign
    key (ITEM_ID) references ITEM on delete cascade for the BID table.

    This option tells the database to maintain the referential integrity of composites
    transparently for all applications accessing the database. Whenever we delete a row in
    the ITEM table, the database will automatically delete any row in the BID table with the
    same ITEM_ID key value. We only need one DELETE statement to remove all dependent
    data recursively, and nothing has to be loaded into application (server) memory.
     */
    @org.hibernate.annotations.OnDelete(
            action = org.hibernate.annotations.OnDeleteAction.CASCADE
    )
    private Set<Bid> bids = new HashSet<>();

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

    public Set<Bid> getBids() {
        return Collections.unmodifiableSet(bids);
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public void removeBid(Bid bid) {
        bids.remove(bid);
    }
}
