package com.example.salonapp;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.AdapterCallBackForCartCount;
import com.example.salonapp.interfaces.AdapterCallBackForSelectedItems;
import com.example.salonapp.views.ListAddedServicesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BookAppointmentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        AdapterCallBackForSelectedItems, AdapterCallBackForCartCount, NavigationView.OnNavigationItemSelectedListener {
    SelectedItems selectedItems = new SelectedItems();
    protected BottomNavigationView bottomNavigationView;
    TextView addedServices, addedProducts;
    int count = 0;
    ListView elv;
    DayScrollDatePicker picker;
    private ArrayList<Cart> productCartList = new ArrayList<>();
    ListAddedServicesAdapter listAddedServicesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle.setText("Book Appointments");

        MovableFloatingActionButton fab = findViewById(R.id.cart_fab);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        fab.setCoordinatorLayout(lp);

        if (null != getIntent() && null != getIntent().getExtras()) {
            if (null != getIntent() && null != getIntent().getExtras().get("selectedItems")) {
                selectedItems = (SelectedItems) getIntent().getExtras().get("selectedItems");
            }
            if (null != getIntent() && null != getIntent().getExtras().get("count")) {
                count = Integer.parseInt(String.valueOf(getIntent().getExtras().get("count")));
            }
            if (null != getIntent() && null != getIntent().getExtras().get("productCartList")) {
                productCartList = (ArrayList<Cart>) getIntent().getExtras().get("productCartList");
            }
        }

        if (null != selectedItems && null != selectedItems.getCartList() && selectedItems.getCartList().size() > 0) {
            addedServices = findViewById(R.id.added_services_text);
            addedServices.setText("Added Services");
            elv = findViewById(R.id.cart_added_service);
            listAddedServicesAdapter = new ListAddedServicesAdapter(getApplicationContext(), count, selectedItems.getCartList(), this, this, true);
            elv.setAdapter(listAddedServicesAdapter);
        }

        if (null != productCartList && productCartList.size() > 0) {
            addedProducts = findViewById(R.id.added_products_text);
            addedProducts.setText("Added Products");
            elv = findViewById(R.id.cart_added_products);
            listAddedServicesAdapter = new ListAddedServicesAdapter(getApplicationContext(), count, productCartList, this, this, false);
            elv.setAdapter(listAddedServicesAdapter);
        }

        picker = findViewById(R.id.datePicker);
        picker.setStartDate(01, 01, 2020);
        picker.setEndDate(31, 12, 2045);

        picker.getSelectedDate(date -> {
            Log.i("HorizontalPicker", "Selected date is " + date.toString());
            selectedItems.setDate(date.toString());
        });

        RecyclerView recyclerView = findViewById(R.id.time_view);

        final String[] times = {"10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "01:00 PM"};

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        HorizontalViewAdapter adapter = new HorizontalViewAdapter(this, times, selectedItems);
        recyclerView.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
                startActivityIfNeeded(savedIntent, 0);
            } else if (itemId == R.id.navigation_services) {
                Intent savedIntent = new Intent(this, GridsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedItems", selectedItems);
                bundle.putSerializable("productCartList", productCartList);
                bundle.putInt("count", count);
                bundle.putSerializable("navigationId", R.id.navigation_services);
                savedIntent.putExtras(bundle);
                startActivityIfNeeded(savedIntent, 0);
            } else if (itemId == R.id.navigation_products) {
                Intent savedIntent = new Intent(this, ProductsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                savedIntent.putExtra("selectedItems", selectedItems);
                savedIntent.putExtra("productCartList", productCartList);
                savedIntent.putExtra("count", count);
                savedIntent.putExtra("navigationId", R.id.navigation_products);
                startActivityIfNeeded(savedIntent, 0);
            } else if (itemId == R.id.navigation_appt) {
                Intent savedIntent = new Intent(this, BookAppointmentActivity.class);
                savedIntent.putExtra("navigationId", R.id.navigation_appt);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                savedIntent.putExtra("selectedItems", selectedItems);
                savedIntent.putExtra("productCartList", productCartList);
                savedIntent.putExtra("count", count);
                savedIntent.putExtra("navigationId", R.id.navigation_products);
                startActivity(savedIntent);
            }
            finish();
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

    public void onClickToAddCalendar(View view) {

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS").parse(selectedItems.getDate() + selectedItems.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", "Ella Salon Appointment");
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


    @Override
    public void getTheSelectedItems() {
        elv.invalidateViews();
    }

    @Override
    public SelectedItems getSelecetedItems() {
        return null;
    }

    public void onClickConfirmOrder(View view) {
        Intent savedIntent = new Intent(this, ConfirmOrderActivity.class);
        savedIntent.putExtra("selectedItems", selectedItems);
        if (productCartList.size() > 0) {
            savedIntent.putExtra("productCartList", productCartList);
        }
        startActivity(savedIntent);
    }

    @Override
    public void onChangeBadgeCount(int cartCount) {
        count = cartCount;
    }

    @Override
    public void setCartList(List<Cart> cartList, boolean isService) {
        if (isService) {
            selectedItems.setCartList(cartList);
        } else {
            productCartList = new ArrayList<>();
            if (!cartList.isEmpty()) {
                productCartList.addAll(cartList);
            }
        }

    }
}



