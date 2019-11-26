package com.example.salonapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.salonapp.dtos.LayoutOptions;
import com.example.salonapp.views.CurvedBottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductsServicesScreen extends FragmentActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{

    private int currentPage = 0;
    private static final long SLIDER_TIMER = 2000;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private Handler handler;

    protected CurvedBottomNavigationView curvedBottomNavigationView;


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

        viewPager = (ViewPager) findViewById(R.id.myViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager = (ViewPager) findViewById(R.id.myViewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

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




       /* int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding_small);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
*/
        final LayoutOptions[] layoutOption = {
                new LayoutOptions(R.string.face_skin, R.drawable.face_skin),
                new LayoutOptions(R.string.hair, R.drawable.hair_cut),
                new LayoutOptions(R.string.makeup, R.drawable.makeup),
                new LayoutOptions(R.string.massages, R.drawable.massage),
                new LayoutOptions(R.string.coloring_styling, R.drawable.hair_styling),
                new LayoutOptions(R.string.bridal, R.drawable.bridal),
                new LayoutOptions(R.string.manicure, R.drawable.manicure),
                new LayoutOptions(R.string.pedicure, R.drawable.pedicure)};


        RecyclerView serviceRecyclerView = (RecyclerView) findViewById(R.id.featured_services);

        LinearLayoutManager serviceslayoutManager = new LinearLayoutManager(this);
        serviceslayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        serviceRecyclerView.setLayoutManager(serviceslayoutManager);

        FeaturedServicesHorizontalViewAdapter servicesAdapter = new FeaturedServicesHorizontalViewAdapter(this, layoutOption);
        serviceRecyclerView.setAdapter(servicesAdapter);


        RecyclerView productRecyclerView = (RecyclerView) findViewById(R.id.featured_products);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        productRecyclerView.setLayoutManager(layoutManager);

        FeaturedProductsHorizontalViewAdapter productAdapter = new FeaturedProductsHorizontalViewAdapter(this, layoutOption);
        productRecyclerView.setAdapter(productAdapter);

        curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(ProductsServicesScreen.this);

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
                savedIntent.putExtra("navigationId",R.id.navigation_featured);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_services) {
                Intent savedIntent = new Intent(this, GridsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                savedIntent.putExtra("navigationId",R.id.navigation_services);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_products) {
                Intent savedIntent = new Intent(this, GridsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                savedIntent.putExtra("navigationId",R.id.navigation_products);
                startActivity(savedIntent);
            }else if (itemId == R.id.navigation_more){
                Intent savedIntent = new Intent(this, ProductsServicesScreen.class);
                savedIntent.putExtra("navigationId",R.id.navigation_more);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(savedIntent);
            }
            //finish();
        }, 300);
        curvedBottomNavigationView.setSelected(true);
        return true;
    }

    public void onClickServicesScreen(View view) {

        Intent savedIntent = new Intent(this, GridsLayoutScreen.class);
        savedIntent.putExtra("navigationId",R.id.navigation_services);
        startActivity(savedIntent);
    }
}

