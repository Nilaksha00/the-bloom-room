package com.example.thebloomroom.model;
import com.google.firebase.firestore.PropertyName;

public class FlowerItem {

    private String id, name, image, description;
    private String size, price;

    public FlowerItem(String id, String description, String image, String name, String price, String size) {
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
        this.size = size;
        this.id = id;
    }

    public FlowerItem() {
        // Empty constructor needed for Firestore
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("image")
    public String getImage() {
        return image;
    }

    @PropertyName("image")
    public void setImage(String image) {
        this.image = image;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("size")
    public String getSize() {
        return size;
    }

    @PropertyName("size")
    public void setSize(String size) {
        this.size = size;
    }

    @PropertyName("price")
    public String getPrice() {
        return price;
    }

    @PropertyName("price")
    public void setPrice(String price) {
        this.price = price;
    }
}
