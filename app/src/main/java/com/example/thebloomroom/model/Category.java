package com.example.thebloomroom.model;

public class Category {
    private String name;
    private String priceMin;
    private String priceMax;

    public Category(String name, String priceMin, String priceMax) {
        this.name = name;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }
}
