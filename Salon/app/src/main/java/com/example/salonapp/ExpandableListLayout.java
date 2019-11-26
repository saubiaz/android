package com.example.salonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.salonapp.dtos.BeautyService;
import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.LayoutOptions;
import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.AdapterCallBackForCartCount;
import com.example.salonapp.views.CurvedBottomNavigationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpandableListLayout extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AdapterCallBackForCartCount{

    protected CurvedBottomNavigationView curvedBottomNavigationView;

    Menu customMenu;
    Button decrementButton, incrementButton;

    TextView textCartItemCount;
    public int mCartItemCount ;
    RelativeLayout actionView;
    ListLayoutAdapter adapter ;
    Context mcontext;
    SelectedItems selectedItems = new SelectedItems();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_layout);


        //THE EXPANDABLE
        ListView elv = (ListView) findViewById(R.id.listView);

        incrementButton = (Button) findViewById(R.id.increment);
        decrementButton = (Button) findViewById(R.id.decrement);
        final ArrayList<BeautyService> beautyServices = new ArrayList<>();
        String layout = getIntent().getStringExtra("layoutName");
        this.setTitle(layout);
        mCartItemCount = getIntent().getExtras().getInt("count");

        if(null!=getIntent() && null!=getIntent().getExtras()) {
            selectedItems.setCartList((ArrayList<Cart>) getIntent().getExtras().get("selectedItems"));
        }


        adapter = new ListLayoutAdapter(getApplicationContext(), beautyServices,this,mCartItemCount,selectedItems);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.activity_custom_action_bar);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
        TextView layoutName = (TextView) findViewById(R.id.title);
        layoutName.setText(layout);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Pedicure").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        List<BeautyService> beautyServiceList = task.getResult().toObjects(BeautyService.class);
                        for (BeautyService beautyService : beautyServiceList) {
                            beautyServices.add(beautyService);
                        }

                    }
                } else {
                    Log.w("Error getting document", task.getException());
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //CREATE AND BIND TO ADAPTER
                elv.setAdapter(adapter);


            }
        });


        RecyclerView productRecyclerView = (RecyclerView) findViewById(R.id.featured_products_expandable_list_layout);

        final LayoutOptions[] layoutOption = {
                new LayoutOptions(R.string.face_skin, R.drawable.face_skin),
                new LayoutOptions(R.string.hair, R.drawable.hair_cut),
                new LayoutOptions(R.string.makeup, R.drawable.makeup),
                new LayoutOptions(R.string.massages, R.drawable.massage),
                new LayoutOptions(R.string.coloring_styling, R.drawable.hair_styling),
                new LayoutOptions(R.string.bridal, R.drawable.bridal),
                new LayoutOptions(R.string.manicure, R.drawable.manicure),
                new LayoutOptions(R.string.pedicure, R.drawable.pedicure)};


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        productRecyclerView.setLayoutManager(layoutManager);

        List<LayoutOptions> layoutOptionsStream = Arrays.stream(layoutOption).filter(layoutOpt -> !layout.equalsIgnoreCase(getString(layoutOpt.getName()))).collect(Collectors.toList());


        ExpandableServicesHorizontalViewAdapter productAdapter = new ExpandableServicesHorizontalViewAdapter(this,layoutOptionsStream,selectedItems );
        productRecyclerView.setAdapter(productAdapter);


        curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(ExpandableListLayout.this);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        customMenu = updateCart(menu,  mCartItemCount >0? String.valueOf(mCartItemCount) :"0");


        RelativeLayout rl_viewBag = (RelativeLayout) menu.findItem(R.id.action_cart).getActionView();

        textCartItemCount = (TextView)rl_viewBag.findViewById(R.id.cart_badge);
        setupBadge(mCartItemCount);
        return true;

    }

    public Menu updateCart(Menu customMenu, String count) {

        MenuItem item = customMenu.findItem(R.id.action_cart);
        actionView =(RelativeLayout) item.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge(mCartItemCount);

        textCartItemCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
            }
        });
        return customMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Do something
        if (item.getItemId() == R.id.action_cart) {

            Intent i = new Intent(this, BookAppointmentActivity.class);
            i.putExtra("cartList", (Serializable)  adapter.getArrayList());// (Serializable) selectedItems.getCartList());
            i.putExtra("layout","Selected Items");
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupBadge(int mCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public void onChangeBadgeCount(int mCartItemCount) {
        setupBadge(mCartItemCount);

    }


    @Override
    protected void onRestart() {
        if (mCartItemCount != 0) {
            invalidateOptionsMenu();
            super.onRestart();
        }
    }

    public void onClickBookAppointments(View view) {
    }

}
