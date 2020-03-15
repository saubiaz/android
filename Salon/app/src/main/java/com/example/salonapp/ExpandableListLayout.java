package com.example.salonapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.salonapp.dtos.BeautyService;
import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.AdapterCallBackForCartCount;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExpandableListLayout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterCallBackForCartCount, ExpandableServicesFragment.OnListFragmentInteractionListener
         {

    protected BottomNavigationView bottomNavigationView;

    Menu customMenu;
    Button decrementButton, incrementButton;

    TextView textCartItemCount;
    public int mCartItemCount;
    RelativeLayout actionView;
    public ListLayoutAdapter adapter;
    private SelectedItems selectedItems = new SelectedItems();
    private List<Cart> productCartList = new ArrayList<>();
    private String service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle.setText(getIntent().getStringExtra("layoutName"));
        //THE EXPANDABLE
        //ListView elv = findViewById(R.id.listView);

        incrementButton = findViewById(R.id.increment);
        decrementButton = findViewById(R.id.decrement);
        final ArrayList<BeautyService> beautyServices = new ArrayList<>();
        String layout = getIntent().getStringExtra("layoutName");
        this.setTitle(layout);
        mCartItemCount = getIntent().getExtras().getInt("count");

        if (null != getIntent() && null != getIntent().getExtras()) {
            if (null != getIntent().getExtras().get("selectedItems")) {
                selectedItems = (SelectedItems) getIntent().getExtras().get("selectedItems");
            }
            if (null != getIntent().getExtras().get("productCartList")) {
                productCartList.addAll((ArrayList<Cart>) getIntent().getExtras().get("productCartList"));
            }
            if(null != getIntent().getExtras().get("service")){
                service = getIntent().getExtras().getString("service");
            }
        }


        Fragment mFragment = new ExpandableServicesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cartList", (Serializable) (CollectionUtils.isEmpty(selectedItems.getCartList()) ? new ArrayList<>() : selectedItems.getCartList()));
        bundle.putInt("cartCount", mCartItemCount);
        bundle.putString("service",service);
        mFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.expandable_frame, mFragment).commit();


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
                if (mCartItemCount > 0) {
                    savedIntent.putExtra("count", mCartItemCount);
                    if (null != selectedItems && selectedItems.getCartList().size() > 0) {
                        savedIntent.putExtra("selectedItems", selectedItems);
                    }
                }
                if (productCartList.size() > 0)
                    savedIntent.putExtra("productCartList", (Serializable) productCartList);
                savedIntent.putExtra("navigationId", R.id.navigation_featured);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_services) {
                Intent savedIntent = new Intent(this, GridsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                if (mCartItemCount > 0) {
                    savedIntent.putExtra("count", mCartItemCount);
                    if (null != selectedItems && selectedItems.getCartList().size() > 0) {
                        savedIntent.putExtra("selectedItems", selectedItems);
                    }
                }
                if (productCartList.size() > 0)
                    savedIntent.putExtra("productCartList", (Serializable) productCartList);
                savedIntent.putExtra("navigationId", R.id.navigation_services);
                if (mCartItemCount > 0)
                    savedIntent.putExtra("count", mCartItemCount);
                startActivity(savedIntent);
            } else if (itemId == R.id.navigation_products) {
                Intent savedIntent = new Intent(this, ProductsLayoutScreen.class);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                if (mCartItemCount > 0) {
                    savedIntent.putExtra("count", mCartItemCount);
                }
                if (null != selectedItems && selectedItems.getCartList().size() > 0) {
                    savedIntent.putExtra("selectedItems", selectedItems);
                }

                if (productCartList.size() > 0)
                    savedIntent.putExtra("productCartList", (Serializable) productCartList);
                savedIntent.putExtra("navigationId", R.id.navigation_products);
                startActivity(savedIntent);
            }  else if (itemId == R.id.navigation_appt) {
                Intent savedIntent = new Intent(this, BookAppointmentActivity.class);
                savedIntent.putExtra("navigationId",R.id.navigation_appt);
                if (mCartItemCount > 0) {
                    savedIntent.putExtra("count", mCartItemCount);
                }
                if (null != selectedItems && selectedItems.getCartList().size() > 0) {
                    savedIntent.putExtra("selectedItems", selectedItems);
                }

                if (productCartList.size() > 0)
                    savedIntent.putExtra("productCartList", (Serializable) productCartList);
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(savedIntent);
            }
            //finish();
            bottomNavigationView.setSelected(true);
            return true;
        });

        DrawerLayout drawer = findViewById(R.id.expandable_drawer_layout);
        NavigationView navigationView = findViewById(R.id.drawer_nav_view);
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Do something
        if (item.getItemId() == R.id.action_cart) {

            Intent i = new Intent(this, BookAppointmentActivity.class);
            // selectedItems.setCartList(adapter.getArrayList());
            i.putExtra("selectedItems", selectedItems);
            i.putExtra("productCartList", (Serializable) productCartList);
            i.putExtra("count", textCartItemCount.getText().toString());
            i.putExtra("layout", "Selected Items");
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
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
    public void onListFragmentInteraction(int cartCount, List<Cart> cartItems) {
        setupBadge(cartCount);
        selectedItems.setCartList(cartItems);
    }
}
