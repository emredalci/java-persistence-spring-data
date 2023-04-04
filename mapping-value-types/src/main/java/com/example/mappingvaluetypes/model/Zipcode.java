package com.example.mappingvaluetypes.model;

public abstract class Zipcode {

    private String value;

    protected Zipcode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
