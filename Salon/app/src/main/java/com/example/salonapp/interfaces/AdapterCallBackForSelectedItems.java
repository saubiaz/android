package com.example.salonapp.interfaces;

import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.SelectedItems;

import java.util.List;

public interface AdapterCallBackForSelectedItems {

    SelectedItems getSelecetedItems();
    void getTheSelectedItems();

    void setCartList(List<Cart> cartList,boolean isService);
}
