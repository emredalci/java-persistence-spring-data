package com.example;

public class TestData {

    private final Long[] identifiers;

    public TestData(Long[] identifiers) {
        this.identifiers = identifiers;
    }

    public Long[] getIdentifiers() {
        return identifiers;
    }

    public Long getFirstId() {
        return identifiers.length > 0 ? identifiers[0] : null;
    }

    public Long getLastId() {
        return identifiers.length > 0 ? identifiers[identifiers.length - 1] : null;
    }
}
