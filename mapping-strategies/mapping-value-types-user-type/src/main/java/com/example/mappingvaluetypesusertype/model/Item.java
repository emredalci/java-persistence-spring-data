package com.example.mappingvaluetypesusertype.model;

import com.example.mappingvaluetypesusertype.converter.MonetaryAmountUserType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(value = MonetaryAmountUserType.class, parameters = {@Parameter(name = "convertTo", value = "USD")})
    //@Columns(columns = {@Column(name = "BUYNOWPRICE_AMOUNT"), @Column(name = "BUYNOWPRICE_CURRENCY")})
    @Column(name = "BUYNOWPRICE")
    private MonetaryAmount buyNowPrice;

    @NotNull
    @Type(value = MonetaryAmountUserType.class, parameters = @Parameter(name = "convertTo", value = "EUR"))
    //@Columns(columns = {@Column(name = "INITIALPRICE_AMOUNT"), @Column(name = "INITIALPRICE_CURRENCY", length = 3)})
    @Column(name = "INITIALPRICE")
    private MonetaryAmount initialPrice;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MonetaryAmount getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(MonetaryAmount buyNowPrice) {
        this.buyNowPrice = buyNowPrice;
    }

    public MonetaryAmount getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(MonetaryAmount initialPrice) {
        this.initialPrice = initialPrice;
    }


}
