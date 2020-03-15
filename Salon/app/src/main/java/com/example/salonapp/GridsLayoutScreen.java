package com.example.salonapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.LayoutOptions;
import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.AdapterCallBackForCartCount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GridsLayoutScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        AdapterCallBackForCartCount, NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;
    protected BottomNavigationView bottomNavigationView;
    Context context;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    RelativeLayout actionView;
    Menu customMenu;
    List<Cart> productCartList = new ArrayList<>();
    SelectedItems selectedItems = new SelectedItems();
    ActionBarDrawerToggle toggle = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grids_layout_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*setSupportActionBar(toolbar);*/
        mTitle.setText("SERVICES");

        mCartItemCount = Objects.requireNonNull(getIntent().getExtras()).getInt("count");

        if (null != getIntent() && null != getIntent().getExtras()) {
            if(null!= getIntent().getExtras().get("selectedItems")) {
                selectedItems = (SelectedItems) getIntent().getExtras().get("selectedItems");
            }
            if(null!=getIntent().getExtras().get("productCartList")) {
                productCartList = (List<Cart>) getIntent().getExtras().get("productCartList");
            }
        }

        final List<LayoutOptions> layoutOptionsList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("BeautyServicesDetails").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    List<LayoutOptions> serviceDetailsList = task.getResult().toObjects(LayoutOptions.class);
                    layoutOptionsList.addAll(serviceDetailsList);
                }
            } else {
                Log.w("Error getting document", task.getException());
            }
        });

        GridView gridView = findViewById(R.id.gridview);
        if (layoutOptionsList.isEmpty()) {
            LayoutOptions layoutOption = new LayoutOptions(R.string.face_skin, R.drawable.face_skin);
            layoutOptionsList.add(layoutOption);
            layoutOption = new LayoutOptions(R.string.hair, R.drawable.hair_cut);
            layoutOptionsList.add(layoutOption);
            layoutOption = new LayoutOptions(R.string.makeup, R.drawable.makeup);
            layoutOptionsList.add(layoutOption);
            layoutOption = new LayoutOptions(R.string.coloring_styling, R.drawable.hair_styling);
            layoutOptionsList.add(layoutOption);
            layoutOption = new LayoutOptions(R.string.bridal, R.drawable.bridal);
            layoutOptionsList.add(layoutOption);
            layoutOption = new LayoutOptions(R.string.manicure, R.drawable.manicure);
            layoutOptionsList.add(layoutOption);
            layoutOption = new LayoutOptions(R.string.pedicure, R.drawable.pedicure);
            layoutOptionsList.add(layoutOption);
        }

        final GridLayout_Adapter layout1Adapter = new GridLayout_Adapter(this, layoutOptionsList);
        gridView.setAdapter(layout1Adapter);


        gridView.setOnItemClickListener((parent, view, position, id) -> {
            LayoutOptions layoutOptions = layoutOptionsList.get(position);

                Intent i = new Intent(getApplicationContext(), ExpandableListLayout.class);
                i.putExtra("layoutName", getString(layoutOptions.getName())); if(mCartItemCount>0) {
                    i.putExtra("count", mCartItemCount);
                    i.putExtra("service",getString(layoutOptions.getName()));
                    if(null!=selectedItems && null!=selectedItems.getCartList() && !selectedItems.getCartList().isEmpty()){
                        i.putExtra("selectedItems",selectedItems);
                    }
                }
                if(null!=productCartList && !productCartList.isEmpty())
                    i.putExtra("productCartList",(Serializable)productCartList);

                startActivity(i);

            // This tells the GridView to redraw itself
            // in turn calling your BooksAdapter's getView method again for each cell
            layout1Adapter.notifyDataSetChanged();
        });

       /* bottomNavigationView = findViewById(R.id.curvedCustomBottomBar);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(GridsLayoutScreen.this);
        bottomNavigationView.setSelectedItemId(Integer.parseInt(Objects.requireNonNull(getIntent().getExtras()).get("navigationId").toString()));*/

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
       /* curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(ProductsServicesScreen.this);*/

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
                boolean isChecked = menuItem.getItemId() == item.getItemId();
                menuItem.setChecked(isChecked);
            }
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_featured) {
                Intent savedIntent = new Intent(this, ProductsServicesScreen.class);
                if(mCartItemCount>0) {
                    savedIntent.putExtra("count", mCartItemCount);
                    if(null!=selectedItems && selectedItems.getCartList().size()>0){
                        savedIntent.putExtra("selectedItems",selectedItems);
                    }
                }
                if(productCartList.size()>0)
                    savedIntent.putExtra("productCartList",(Serializable)productCartList);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                savedIntent.putExtra("navigationId", R.id.navigation_featured);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_services) {
                Intent savedIntent = new Intent(this, GridsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                if(mCartItemCount>0) {
                    savedIntent.putExtra("count", mCartItemCount);
                    if(null!=selectedItems && selectedItems.getCartList().size()>0){
                        savedIntent.putExtra("selectedItems",selectedItems);
                    }
                }
                if(productCartList.size()>0)
                    savedIntent.putExtra("productCartList",(Serializable)productCartList);
                savedIntent.putExtra("navigationId", R.id.navigation_services);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_products) {
                Intent savedIntent = new Intent(this, ProductsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                if(mCartItemCount>0) {
                    savedIntent.putExtra("count", mCartItemCount);
                    if(null!=selectedItems && selectedItems.getCartList().size()>0){
                        savedIntent.putExtra("selectedItems",selectedItems);
                    }
                }
                if(productCartList.size()>0)
                    savedIntent.putExtra("productCartList",(Serializable)productCartList);
                savedIntent.putExtra("navigationId", R.id.navigation_products);
                startActivity(savedIntent);
            }  else if (itemId == R.id.navigation_appt) {
                Intent savedIntent = new Intent(this, BookAppointmentActivity.class);
                savedIntent.putExtra("navigationId",R.id.navigation_appt);
                if(mCartItemCount>0) {
                    savedIntent.putExtra("count", mCartItemCount);
                    if(null!=selectedItems && selectedItems.getCartList().size()>0){
                        savedIntent.putExtra("selectedItems",selectedItems);
                    }
                }
                if(productCartList.size()>0)
                    savedIntent.putExtra("productCartList",(Serializable)productCartList);
                savedIntent.putExtra("navigationId", R.id.navigation_products);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(savedIntent);
            }
            //finish();
            bottomNavigationView.setSelected(true);
            return true;
        });

        DrawerLayout drawer = findViewById(R.id.grids_drawer_layout);
        NavigationView navigationView = findViewById(R.id.drawer_nav_view);

         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        customMenu = updateCart(menu);


        RelativeLayout rl_viewBag = (RelativeLayout) menu.findItem(R.id.action_cart).getActionView();

        textCartItemCount = rl_viewBag.findViewById(R.id.cart_badge);
        setupBadge(mCartItemCount);
        return true;

    }

    public Menu updateCart(Menu customMenu) {

        MenuItem item = customMenu.findItem(R.id.action_cart);
        actionView = (RelativeLayout) item.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        setupBadge(mCartItemCount);

        textCartItemCount.setOnClickListener(v -> onOptionsItemSelected(item));
        return customMenu;
    }

    @Override
    public void onChangeBadgeCount(int mCartItemCount) {
        setupBadge(mCartItemCount);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Do something
        if (item.getItemId() == R.id.action_cart) {

            Intent i = new Intent(this, BookAppointmentActivity.class);
            //i.putExtra("productCartList", (Serializable) productCartList);
            //i.putExtra("selectedItems", selectedItems);
            i.putExtra("count", textCartItemCount.getText().toString());
            i.putExtra("layout", "Selected Items");
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        mCartItemCount = bundle.getInt("count", 0);
        productCartList = (List<Cart>) bundle.get("productCartList");
        selectedItems = (SelectedItems) bundle.get("selectedItems");
        if(mCartItemCount>0)setupBadge(mCartItemCount);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
