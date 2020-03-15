package com.example.salonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.LayoutOptions;
import com.example.salonapp.views.ChildExpandableListAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    List<Cart> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        expandableListView =  findViewById(R.id.expandableListView_my_orders);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("OrderDetails").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    List<Cart> expandableListTitles = task.getResult().toObjects(Cart.class);
                    expandableListTitle.addAll(expandableListTitles);
                }
            } else {
                Log.w("Error getting document", task.getException());
            }
        });
        expandableListTitle = new ArrayList(expandableListDetail.keySet());
        ChildExpandableListAdapter adapter = new ChildExpandableListAdapter(getApplicationContext(),null,null);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupExpandListener(groupPosition -> Toast.makeText(getApplicationContext(),
                expandableListTitle.get(groupPosition) + " List Expanded.",
                Toast.LENGTH_SHORT).show());

        expandableListView.setOnGroupCollapseListener(groupPosition -> Toast.makeText(getApplicationContext(),
                expandableListTitle.get(groupPosition) + " List Collapsed.",
                Toast.LENGTH_SHORT).show());

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Toast.makeText(
                    getApplicationContext(),
                    expandableListTitle.get(groupPosition)
                            + " -> "
                            + expandableListDetail.get(
                            expandableListTitle.get(groupPosition)).get(
                            childPosition), Toast.LENGTH_SHORT
            ).show();
            return false;
        });

    }
}
