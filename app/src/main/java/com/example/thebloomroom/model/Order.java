package com.example.thebloomroom.model;

public class Order {
    private String status;
    private String id;
    private String address;
    private String flowerID;
    private String flowerName;
    private String quantity;
    private String total;
    private String userID;
    private String userName;
    private String date;

    public Order(String id, String address, String flowerID, String flowerName, String quantity, String total, String userID, String userName, String status, String date) {
        this.id = id;
        this.address = address;
        this.flowerID = flowerID;
        this.flowerName = flowerName;
        this.quantity = quantity;
        this.total = total;
        this.userID = userID;
        this.userName = userName;
        this.status = status;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFlowerID() {
        return flowerID;
    }

    public void setFlowerID(String flowerID) {
        this.flowerID = flowerID;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public void setFlowerName(String flowerName) {
        this.flowerName = flowerName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
