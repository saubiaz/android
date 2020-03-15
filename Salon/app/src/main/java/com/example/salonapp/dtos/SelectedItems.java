package com.example.salonapp.dtos;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SelectedItems implements Serializable {

    private List<Cart> cartList;
    private String date;
    private String time;

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
      /*  if (null != getCartList()) {
            if (getCartList().isEmpty()) {
                this.cartList = cartList;
            } else {
                Objects.requireNonNull(getCartList()).addAll(cartList);
                this.cartList = getCartList();
            }
        }else{
            this.cartList = cartList;
        }*/
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
