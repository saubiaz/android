package com.example.salonapp.dtos;

import java.io.Serializable;
import java.util.Objects;

public class Cart implements Serializable {

    private String serviceId;
    private String serviceName;
    private Integer resourceId;
    private String price;
    private Integer quantity=0;


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }


    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return
                Objects.equals(serviceName, cart.serviceName) &&
                Objects.equals(price, cart.price);

    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, serviceName, price, quantity);
    }
}
