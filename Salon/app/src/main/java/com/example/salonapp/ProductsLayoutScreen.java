package com.example.salonapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.ProductDetails;
import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.AdapterCallBackForCartCount;
import com.example.salonapp.views.CurvedBottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProductsLayoutScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AdapterCallBackForCartCount
,NavigationView.OnNavigationItemSelectedListener {

    ListLayoutAdapter adapter;
    public int mCartItemCount=0;
    Menu customMenu;
    SelectedItems selectedItems = new SelectedItems();
    protected  BottomNavigationView bottomNavigationView;
    TextView textCartItemCount, counter;
    RelativeLayout actionView;
    AdapterCallBackForCartCount adapterCallBackForCartCount;
    public List<Cart> productCartList = new ArrayList<>();
    private ArrayList<ProductDetails> productDetailsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_layout_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle.setText("PRODUCTS");

        final List<ProductDetails> productsList = new ArrayList<>();

        mCartItemCount = Objects.requireNonNull(getIntent().getExtras()).getInt("count");

        if (null != getIntent() && null != getIntent().getExtras()) {
            if(null!= getIntent().getExtras().get("selectedItems")) {
                selectedItems = (SelectedItems) getIntent().getExtras().get("selectedItems");
            }
            if(null!=getIntent().getExtras().get("productCartList")) {
                productCartList = (List<Cart>) getIntent().getExtras().get("productCartList");
            }
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ProductDetails").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    List<ProductDetails> productDetailsList = task.getResult().toObjects(ProductDetails.class);
                    productsList.addAll(productDetailsList);
                }
            } else {
                Log.w("Error getting document", task.getException());
            }
        });

        GridView gridView = findViewById(R.id.productsgridview);

        if (productsList.isEmpty()) {
            ProductDetails productDetail = new ProductDetails(R.drawable.face_skin, getString(R.string.face_skin), 20d);
            productsList.add(productDetail);
            productDetail = new ProductDetails(R.drawable.hair_cut, getString(R.string.hair), 30d);
            productsList.add(productDetail);
            productDetail = new ProductDetails(R.drawable.makeup, getString(R.string.makeup), 40d);
            productsList.add(productDetail);
            productDetail = new ProductDetails(R.drawable.hair_styling, getString(R.string.coloring_styling), 50d);
            productsList.add(productDetail);
            productDetail = new ProductDetails(R.drawable.bridal, getString(R.string.bridal), 60d);
            productsList.add(productDetail);
            productDetail = new ProductDetails(R.drawable.manicure, getString(R.string.manicure), 70d);
            productsList.add(productDetail);
            productDetail = new ProductDetails(R.drawable.pedicure, getString(R.string.pedicure), 80d);
            productsList.add(productDetail);

        }

        final ProductsGridLayout_Adapter layout1Adapter = new ProductsGridLayout_Adapter(this, productsList);
        gridView.setAdapter(layout1Adapter);


        gridView.setOnItemClickListener((parent, view, position, id) -> {
            ProductDetails productDetails = productsList.get(position);
            // This tells the GridView to redraw itself
            // in turn calling your BooksAdapter's getView method again for each cell
            Cart cart = getSelectedListItem(view, productCartList,productDetails);
            if (null == productCartList) new ArrayList<>();
            if (!productCartList.contains(cart)) {
                productCartList.add(cart);
            }
            mCartItemCount++;
            onChangeBadgeCount(mCartItemCount);
            layout1Adapter.notifyDataSetChanged();
        });

        /*curvedBottomNavigationView = findViewById(R.id.curvedCustomBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(ProductsLayoutScreen.this);
        curvedBottomNavigationView.setSelectedItemId(Integer.parseInt(Objects.requireNonNull(getIntent().getExtras()).get("navigationId").toString()));*/

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
       /* curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(ProductsServicesScreen.this);*/

        bottomNavigationView.setOnNavigationItemSelectedListener(item-> {
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
                if(mCartItemCount>0) {
                    savedIntent.putExtra("count", mCartItemCount);
                    if(null!=selectedItems && null!=selectedItems.getCartList() && !selectedItems.getCartList().isEmpty()){
                        savedIntent.putExtra("selectedItems",selectedItems);
                    }
                }
                if(!productCartList.isEmpty())
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
            } else if (itemId == R.id.navigation_appt) {
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
                savedIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(savedIntent);
            }
            //finish();
            bottomNavigationView.setSelected(true);
            return true;
        });

        DrawerLayout drawer = findViewById(R.id.product_drawer_layout);
        NavigationView navigationView = findViewById(R.id.product_nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private Cart getSelectedListItem(View v, List<Cart> cartList, ProductDetails productDetails) {
        Cart cart;
        Optional<Cart> availableProduct = null;
        if(null!=cartList) {
            availableProduct   = cartList.stream().filter(c -> c.getResourceId() == productDetails.getResourceId()).findFirst();
        }
        if (null!=availableProduct && availableProduct.isPresent()) {
            cart = availableProduct.get();
            cart.setQuantity(cart.getQuantity() + 1);
        } else {
            cart = new Cart();
            //cart.setServiceId(beautyServiceArrayList.get(position).getServices().get("serviceId"));
            cart.setPrice(String.valueOf(productDetails.getPrice()));
            cart.setServiceName(StringUtils.capitalize(productDetails.getDescription().toLowerCase()));
            cart.setResourceId(productDetails.getResourceId());
            cart.setQuantity(1);
        }
        return cart;
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
            i.putExtra("productCartList", (Serializable) productCartList);
            i.putExtra("selectedItems", selectedItems);
            i.putExtra("count",textCartItemCount.getText().toString());
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
        super.onRestart();

        if (mCartItemCount != 0) {
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mCartItemCount= intent.getExtras().getInt("count",0);
        productCartList = (List<Cart>) intent.getExtras().get("productCartList");
        selectedItems = (SelectedItems) intent.getExtras().get("selectedItems");

    }
    @Override
    protected void onResume(){
        super.onResume();
        getIntent().getExtras().getInt("count",0);
    }

}
