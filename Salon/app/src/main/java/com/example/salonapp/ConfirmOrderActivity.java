package com.example.salonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.AdapterCallBackForSelectedItems;
import com.example.salonapp.views.ListConfirmLayoutAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.Objects;

public class ConfirmOrderActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener {


    ListView listView;
    SelectedItems selectedItems = new SelectedItems();
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        /*setSupportActionBar(toolbar);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle.setText("Order Confirmation");

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

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
       /* curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(ProductsServicesScreen.this);*/

        bottomNavigationView.setOnNavigationItemSelectedListener(item-> {
            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
                boolean isChecked = menuItem.getItemId() == item.getItemId();
                menuItem.setChecked(isChecked);
            }
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_featured) {
                Intent savedIntent = new Intent(this, ProductsServicesScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                savedIntent.putExtra("navigationId", R.id.navigation_featured);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_services) {
                Intent savedIntent = new Intent(this, GridsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                savedIntent.putExtra("navigationId", R.id.navigation_services);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_products) {
                Intent savedIntent = new Intent(this, ProductsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                savedIntent.putExtra("navigationId", R.id.navigation_products);
                startActivity(savedIntent);
            } /*else if (itemId == R.id.navigation_more) {
                            // Intent savedIntent = new Intent(this, NavigationDrawerAcitivity.class);
                            drawer.openDrawer(GravityCompat.START);
                            //savedIntent.putExtra("navigationId",R.id.navigation_more);
                            //savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //startActivity(savedIntent);
                        }*/
            //finish();
            bottomNavigationView.setSelected(true);
            return true;
        });

        DrawerLayout drawer = findViewById(R.id.book_aapt_drawer_layout);
        NavigationView navigationView = findViewById(R.id.drawer_nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onClickToRegister(View view) {

        Intent intent = new Intent(this,SignUpScreen.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;


        if (id == R.id.nav_profile) {
            fragment = new NavigationProfile();
        } else if (id == R.id.nav_offers) {
            fragment = new NavigationOffersFragment();
        } else if (id == R.id.nav_my_orders) {
            fragment = new NavigationMyOrdersFragment();
        } else if (id == R.id.nav_refer_frend) {
            fragment = new NavigationReferAFriend();
        } else if (id == R.id.nav_contact) {
            fragment = new NavigationContact();
        } else if (id == R.id.nav_rate_us) {
            fragment = new NavigationRateUs();
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.expandable_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.expandable_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
