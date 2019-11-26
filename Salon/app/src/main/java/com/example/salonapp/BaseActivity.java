package com.example.salonapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity /*implements BottomNavigationView.OnNavigationItemSelectedListener */{
    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footer);

    }


   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_featured) {
                startActivity(new Intent(this, GridsLayoutScreen.class));
            } else if (itemId == R.id.navigation_services) {
                startActivity(new Intent(this, LoginScreen.class));
            } else if (itemId == R.id.navigation_appointments) {
              startActivity(new Intent(this, SignUpScreen.class));
            }
            finish();
        }, 300);
        return true;
    }
*/
}
