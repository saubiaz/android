package com.example.salonapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salonapp.dtos.LayoutOptions;
import com.example.salonapp.views.CurvedBottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class GridsLayoutScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected CurvedBottomNavigationView curvedBottomNavigationView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grids_layout_screen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.activity_custom_action_bar);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
        TextView layoutName = (TextView) findViewById(R.id.title);
        layoutName.setText("SERVICES");

        final LayoutOptions[] layoutOption = {
                new LayoutOptions(R.string.face_skin, R.drawable.face_skin),
                new LayoutOptions(R.string.hair, R.drawable.hair_cut),
                new LayoutOptions(R.string.makeup, R.drawable.makeup),
                new LayoutOptions(R.string.massages, R.drawable.massage),
                new LayoutOptions(R.string.coloring_styling, R.drawable.hair_styling),
                new LayoutOptions(R.string.bridal, R.drawable.bridal),
                new LayoutOptions(R.string.manicure, R.drawable.manicure),
                new LayoutOptions(R.string.pedicure, R.drawable.pedicure)};

        GridView gridView = (GridView) findViewById(R.id.gridview);

        final Layout1_Adapter layout1Adapter = new Layout1_Adapter(this, layoutOption);
        gridView.setAdapter(layout1Adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                LayoutOptions layoutOptions = layoutOption[position];


                if (getString(layoutOptions.getName()).equals(getString(R.string.pedicure))) {
                    Intent i = new Intent(getApplicationContext(), ExpandableListLayout.class);
                    i.putExtra("layoutName", getString(layoutOptions.getName()));
                    startActivity(i);
                }

                // This tells the GridView to redraw itself
                // in turn calling your BooksAdapter's getView method again for each cell
                layout1Adapter.notifyDataSetChanged();
            }
        });

        curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(GridsLayoutScreen.this);
        curvedBottomNavigationView.setSelectedItemId(Integer.parseInt(Objects.requireNonNull(getIntent().getExtras()).get("navigationId").toString()));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // uncheck the other items.
         int mMenuId = item.getItemId();
        for (int i = 0; i < curvedBottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = curvedBottomNavigationView.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }
        curvedBottomNavigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_featured) {
                Intent savedIntent = new Intent(this, ProductsServicesScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(savedIntent);
            } else if (itemId == R.id.navigation_services) {
                Intent savedIntent = new Intent(this, GridsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_products) {
                Intent savedIntent = new Intent(this, ProductsServicesScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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




}
