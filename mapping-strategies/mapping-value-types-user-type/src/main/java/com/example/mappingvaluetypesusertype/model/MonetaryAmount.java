package com.example.mappingvaluetypesusertype.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;


/*
   This value-typed class should be Serializable: When Hibernate stores entity
   instance data in the shared second-level cache , it disassembles
   the entity's state. If an entity has a MonetaryAmount property, the serialized
   representation of the property value will be stored in the second-level cache region. When entity
   data is retrieved from the cache region, the property value will be deserialized and reassembled.
 */
public record MonetaryAmount(BigDecimal value, Currency currency) implements Serializable {

    public static MonetaryAmount fromString(String s){
        String[] split = s.split(" ");
        return new MonetaryAmount(
                new BigDecimal(split[0]),
                Currency.getInstance(split[1])
        );
    }

    @Override
    public String toString(){
        return value + " " + currency;
    }


}
