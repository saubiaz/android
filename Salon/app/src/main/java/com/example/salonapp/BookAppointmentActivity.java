package com.example.salonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salonapp.decorations.SpacesItemDecoration;
import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.AdapterCallBackForCartCount;
import com.example.salonapp.interfaces.AdapterCallBackForSelectedItems;
import com.example.salonapp.views.CurvedBottomNavigationView;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookAppointmentActivity extends AppCompatActivity implements DatePickerListener, BottomNavigationView.OnNavigationItemSelectedListener, AdapterCallBackForSelectedItems {
    SelectedItems selectedItems = new SelectedItems();
    protected CurvedBottomNavigationView curvedBottomNavigationView;;
    ListView elv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.activity_custom_action_bar);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
        TextView layoutName = (TextView) findViewById(R.id.title);
        layoutName.setText("Book Appointments");

        if(null!=getIntent() && null!=getIntent().getExtras()) {
            selectedItems.setCartList((ArrayList<Cart>) getIntent().getExtras().get("cartList"));
        }
        elv = (ListView) findViewById(R.id.cart_added_services);
        ListCartLayoutAdapter listLayoutAdapter = new ListCartLayoutAdapter(getApplicationContext(), selectedItems,this);
        elv.setAdapter(listLayoutAdapter);

        // find the picker
        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);

        // initialize it and attach a listener
        picker.setListener((DatePickerListener) this)
                .setDateSelectedColor(ContextCompat.getColor(getApplicationContext(), R.color.pink))
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getColor(R.color.colorPrimary))
                .setTodayDateTextColor(getColor(R.color.colorPrimary))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.BLACK)
                .showTodayButton(false)
                .init();

        // or on the View directly after init was completed
        picker.setBackgroundColor(Color.LTGRAY);
        picker.setDate(new DateTime().plusDays(4));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.time_view);

       /* int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding_small);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
*/
        final String[] times = {"10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "01:00 PM"};

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        HorizontalViewAdapter adapter = new HorizontalViewAdapter(this, times, selectedItems);
        recyclerView.setAdapter(adapter);
        curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public void onDateSelected(@NonNull final DateTime dateSelected) {
        // log it for demo
        Log.i("HorizontalPicker", "Selected date is " + dateSelected.toString());
        selectedItems.setDate(dateSelected.toString());
    }


    public void onClickToAddCalendar(View view){

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS").parse(selectedItems.getDate()+selectedItems.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime",cal.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", "Ella Salon Appointment");
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        curvedBottomNavigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_featured) {
                Intent savedIntent = new Intent(this, ImageSlidingScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_services) {
                Intent savedIntent = new Intent(this, GridsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_products) {
                Intent savedIntent = new Intent(this, VerifyOrder.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                savedIntent.putExtra("selectedItems",selectedItems);
                startActivity(savedIntent);

            }else if (itemId == R.id.navigation_more){
                Intent savedIntent = new Intent(this, ProductsServicesScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(savedIntent);
            }
            //finish();
        }, 300);
        return true;
    }



        @Override
        public void getTheSelectedItems() {
            elv.invalidateViews();
        }



    public void onClickConfirmOrder(View view){
        Intent savedIntent = new Intent(this, ConfirmOrderActivity.class);
        savedIntent.putExtra("selectedItems",selectedItems);
        startActivity(savedIntent);
    }
    }



