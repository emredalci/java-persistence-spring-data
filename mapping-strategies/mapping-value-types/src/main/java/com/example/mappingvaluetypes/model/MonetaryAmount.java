package com.example.mappingvaluetypes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

public record MonetaryAmount(BigDecimal value, Currency currency) implements Serializable {

    public static MonetaryAmount fromString(String s) {
        String[] split = s.split(" ");
        return new MonetaryAmount(new BigDecimal(split[0]), Currency.getInstance(split[1]));
    }

    @Override
    public String toString() {
        return value + " " + currency;
    }
}
