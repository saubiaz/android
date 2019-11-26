package com.example.salonapp.dtos;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.List;

public class SelectedItems implements Serializable {

    List<Cart> cartList;
    private String date;
    private String time;

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }




}
