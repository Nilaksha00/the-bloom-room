package com.example.thebloomroom.model;

public class FlowerItem {

    private String id, name, image, description, category;
    private String size, price;

    public FlowerItem(String id, String description, String image, String name, String price, String size) {
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
        this.size = size;
        this.id = id;
    }

    public FlowerItem(String id, String name, String image, String description, String category, String size, String price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.category = category;
        this.size = size;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
