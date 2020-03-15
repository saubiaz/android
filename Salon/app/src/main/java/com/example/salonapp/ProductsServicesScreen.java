package com.example.salonapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.salonapp.dtos.LayoutOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ProductsServicesScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int currentPage = 0;
    private static final long SLIDER_TIMER = 2000;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private Handler handler;
    private DrawerLayout drawer;
    protected BottomNavigationView bottomNavigationView;
    ActionBarDrawerToggle toggle = null;
    NavigationView navigationView ;

    private boolean isCountDownTimerActive = false;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (!isCountDownTimerActive) {
                automateSlider();
            }

            handler.postDelayed(runnable, 1000);
            // our runnable should keep running for every 1000 milliseconds (1 seconds)
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_services_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("SERVICES & PRODUCTS");
        applyViewPager();

        final LayoutOptions[] layoutOption = {
                new LayoutOptions(R.string.face_skin, R.drawable.face_skin),
                new LayoutOptions(R.string.hair, R.drawable.hair_cut),
                new LayoutOptions(R.string.makeup, R.drawable.makeup),
                new LayoutOptions(R.string.massages, R.drawable.massage),
                new LayoutOptions(R.string.coloring_styling, R.drawable.hair_styling),
                new LayoutOptions(R.string.bridal, R.drawable.bridal),
                new LayoutOptions(R.string.manicure, R.drawable.manicure),
                new LayoutOptions(R.string.pedicure, R.drawable.pedicure)};


        RecyclerView serviceRecyclerView = findViewById(R.id.featured_services);

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
            } else if (itemId == R.id.navigation_appt) {
                Intent savedIntent = new Intent(this, BookAppointmentActivity.class);
                savedIntent.putExtra("navigationId", R.id.navigation_appt);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(savedIntent);
            }
            //finish();
            bottomNavigationView.setSelected(true);
            return true;
        });

        DrawerLayout drawer = findViewById(R.id.product_drawer_layout);


         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.drawer_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        LinearLayoutManager servicesLayoutManager = new LinearLayoutManager(this);
        servicesLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        serviceRecyclerView.setLayoutManager(servicesLayoutManager);

        FeaturedServicesHorizontalViewAdapter servicesAdapter = new FeaturedServicesHorizontalViewAdapter(this, layoutOption);
        serviceRecyclerView.setAdapter(servicesAdapter);


        RecyclerView productRecyclerView = findViewById(R.id.featured_products);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        productRecyclerView.setLayoutManager(layoutManager);

        FeaturedProductsHorizontalViewAdapter productAdapter = new FeaturedProductsHorizontalViewAdapter(this, layoutOption);
        productRecyclerView.setAdapter(productAdapter);
    }

    private void applyViewPager() {
        viewPager = findViewById(R.id.myViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager = findViewById(R.id.myViewPager);

        sliderDotspanel = findViewById(R.id.slider_dots);

        handler = new Handler();

        handler.postDelayed(runnable, 1000);
        runnable.run();

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void automateSlider() {
        isCountDownTimerActive = true;
        new CountDownTimer(SLIDER_TIMER, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                int nextSlider = currentPage + 1;


                if (nextSlider == 4) {
                    nextSlider = 0; // if it's last Image, let it go to the first image
                }

                viewPager.setCurrentItem(nextSlider);
                isCountDownTimerActive = false;
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Kill this background task once the activity has been killed
        handler.removeCallbacks(runnable);
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
           fragmentTransaction.replace(R.id.nav_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.product_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.grids_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

