package com.example.salonapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.views.ListConfirmLayoutAdapter;

import java.text.DecimalFormat;
import java.util.Objects;

public class ConfirmOrderActivity extends AppCompatActivity {


    ListView listView;
    SelectedItems selectedItems = new SelectedItems();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.activity_custom_action_bar);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
        TextView layoutName = findViewById(R.id.title);
        layoutName.setText("Order Confirmation");

        if(null!=getIntent().getExtras()){
            selectedItems = (SelectedItems) Objects.requireNonNull(getIntent().getExtras()).get("selectedItems");
        }else{
            selectedItems = null;
        }

        ListConfirmLayoutAdapter listConfirmOrderLayoutAdapter = new ListConfirmLayoutAdapter(getApplicationContext(),selectedItems);
        listView = findViewById(R.id.confirm_order);
        listView.setAdapter(listConfirmOrderLayoutAdapter);
        Double amount = selectedItems.getCartList().stream().mapToDouble(cart -> (cart.getQuantity()*Double.parseDouble(cart.getPrice()))).sum();

        TextView service_count =  findViewById(R.id.confirm_service_count_value);
        String count = String.valueOf(selectedItems.getCartList().stream().map(cart -> cart.getQuantity()).reduce(0, Integer::sum));
        service_count.setText( String.valueOf(amount));

        TextView gst_value = findViewById(R.id.confirm_gst_value);

        Double gst_amount_with_percentage = (amount*15)/100;
        gst_value.setText(String.valueOf(gst_amount_with_percentage));

        TextView total_amount = findViewById(R.id.total_value);
        Double total_value = amount + gst_amount_with_percentage;
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        total_amount.setText(df.format(total_value));
    }

    public void onClickToRegister(View view) {

        Intent intent = new Intent(this,SignUpScreen.class);
        startActivity(intent);
    }
}
