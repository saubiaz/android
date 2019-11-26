package com.example.salonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.HorizontalScrollView;

import java.util.List;

public class HorizontalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_view);

       /* HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.time_view);

        final String[] times = {"10:00 AM","10:30 AM", "11:00 AM" , "11:30 AM" , "12:00 PM" , "12:30 PM" , "01:00 PM" };

        HorizontalViewAdapter horizontalViewAdapter = new HorizontalViewAdapter(this,times,);*/
    }
}
