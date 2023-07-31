package com.example;

public class ConcurrencyTestData {
    private TestData categories;
    private TestData items;

    public TestData getCategories() {
        return categories;
    }

    public TestData getItems() {
        return items;
    }

    public void setCategories(TestData categories) {
        this.categories = categories;
    }

    public void setItems(TestData items) {
        this.items = items;
    }
}
