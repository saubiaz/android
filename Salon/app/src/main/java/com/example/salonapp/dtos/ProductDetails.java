package com.example.salonapp.dtos;

public class ProductDetails {

    private int resourceId;
    private String description;
    private Double price;


    public ProductDetails(int resourceId, String description, Double price) {
        this.resourceId = resourceId;
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
