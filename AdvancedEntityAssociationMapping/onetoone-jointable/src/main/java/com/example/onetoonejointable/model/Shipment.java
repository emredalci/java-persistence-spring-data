
package com.example.onetoonejointable.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @CreationTimestamp
    private LocalDateTime createdOn;

    @NotNull
    private ShipmentState shipmentState = ShipmentState.TRANSIT;

    //we could offer an escrow service. Sellers
    //would use this service to create a trackable shipment once the auction ends. The
    //buyer would pay the price of the auction item to a trustee (us), and we’d inform the
    //seller that the money is available. Once the shipment arrives and the buyer accepts it,
    //we’d transfer the money to the seller.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ITEM_SHIPMENT", // Required!
            joinColumns =
            @JoinColumn(name = "SHIPMENT_ID"),  // Defaults to ID
            inverseJoinColumns =
            @JoinColumn(name = "ITEM_ID",  // Defaults to AUCTION_ID
                    nullable = false,
                    unique = true)
    )
    private Item auction;

    public Shipment() {
    }

    public Shipment(Item auction) {
        this.auction = auction;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public ShipmentState getShipmentState() {
        return shipmentState;
    }

    public void setShipmentState(ShipmentState shipmentState) {
        this.shipmentState = shipmentState;
    }

    public Item getAuction() {
        return auction;
    }

    public void setAuction(Item auction) {
        this.auction = auction;
    }
}
