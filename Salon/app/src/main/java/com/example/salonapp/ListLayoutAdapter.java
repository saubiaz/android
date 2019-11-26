package com.example.salonapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.salonapp.dtos.BeautyService;
import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.AdapterCallBackForCartCount;
import com.google.android.gms.common.util.CollectionUtils;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ListLayoutAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BeautyService> beautyServiceArrayList;
    private LayoutInflater inflater;
    public List<Cart> cartList;

    AdapterCallBackForCartCount adapterCallback;
    public int mCartItemCount;
    TextView counter;

    int count = 0;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    public ListLayoutAdapter(Context context, ArrayList<BeautyService> beautyServiceArrayList, AdapterCallBackForCartCount adapterCallback, int count, SelectedItems selectedItems) {
        this.context = context;
        this.beautyServiceArrayList = beautyServiceArrayList;
        this.adapterCallback = adapterCallback;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCartItemCount = count;
        cartList = CollectionUtils.isEmpty(selectedItems.getCartList())?new ArrayList<>():selectedItems.getCartList();
    }


    // get Service
    @Override
    public int getCount() {
        return beautyServiceArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int beautyServiceArrayListPosition, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.activity_list_layout_options, null);

        TextView serviceName = (TextView) convertView.findViewById(R.id.service_name);

        serviceName.setText(beautyServiceArrayList.get(beautyServiceArrayListPosition).getServices().get("name"));

        TextView desc = (TextView) convertView.findViewById(R.id.description);

        desc.setText(beautyServiceArrayList.get(beautyServiceArrayListPosition).getServices().get("description"));

        TextView price = (TextView) convertView.findViewById(R.id.price);
        price.setText(context.getResources().getString(R.string.rupee) + " " + beautyServiceArrayList.get(beautyServiceArrayListPosition).getServices().get("price"));

        TextView counter = convertView.findViewById(R.id.counter);
        counter.setText("0");
        hide(convertView);
        Button incrementButton = convertView.findViewById(R.id.increment);

        Button decrementButton = convertView.findViewById(R.id.decrement);

        incrementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //createCart
                Cart cart = getSelectedListItem(v, cartList);
                counter.setText(cart.getQuantity().toString());
                if (null == cartList) new ArrayList<>();
                if (!cartList.contains(cart)) {
                    cartList.add(cart);
                }
                //incrementButton.setVisibility(View.INVISIBLE);
                if (cart.getQuantity() >= 0) {
                    counter.setVisibility(View.VISIBLE);
                    decrementButton.setVisibility(View.VISIBLE);
                }
                mCartItemCount++;
                adapterCallback.onChangeBadgeCount(mCartItemCount);

            }

            private Cart getSelectedListItem(View v, List<Cart> cartList) {
                View parentRow = (View) v.getParent();
                Cart cart;
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);
                Optional<Cart> availableService = cartList.stream().filter(c -> c.getServiceName().equalsIgnoreCase(beautyServiceArrayList.get(position).getServices().get("name"))).findFirst();
                if (availableService.isPresent()) {
                    cart = availableService.get();
                    cart.setQuantity(cart.getQuantity() + 1);
                    beautyServiceArrayList.get(position).getServices().put("quantity", String.valueOf(cart.getQuantity()));
                } else {
                    cart = new Cart();
                    cart.setServiceId(beautyServiceArrayList.get(position).getServices().get("serviceId"));
                    cart.setPrice(beautyServiceArrayList.get(position).getServices().get("price"));
                    cart.setServiceName(beautyServiceArrayList.get(position).getServices().get("name"));
                    /*cart.setResourceId(Integer.parseInt(beautyServiceArrayList.get(position).getServices().get("resourceId")));*/
                    if (beautyServiceArrayList.get(position).getServices().containsKey("quantity")) {
                        beautyServiceArrayList.get(position).getServices().put("quantity", String.valueOf(Integer.parseInt(beautyServiceArrayList.get(position).getServices().get("quantity")) + 1));
                    } else {
                        beautyServiceArrayList.get(position).getServices().put("quantity", "1");
                    }
                    cart.setQuantity(Integer.parseInt(beautyServiceArrayList.get(position).getServices().get("quantity")));
                }
                return cart;
            }
        });


        decrementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cart cart = null;

                if (!CollectionUtils.isEmpty(cartList)) {
                    cart = getSelectedListItem(v, cartList);
                    counter.setText(cart.getQuantity().toString());

                }
                if (cart.getQuantity() <= 0) {
                    counter.setVisibility(View.INVISIBLE);
                    decrementButton.setVisibility(View.INVISIBLE);
                    cartList.remove(cart);
                }
                // incrementButton.setVisibility(View.VISIBLE);
                mCartItemCount--;
                adapterCallback.onChangeBadgeCount(mCartItemCount);
            }

            private Cart getSelectedListItem(View v, List<Cart> cartList) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                Cart cart = new Cart();
                final int position = listView.getPositionForView(parentRow);
                //  cart.setServiceId(beautyServiceArrayList.get(position).getServices().get("serviceId"));
                Optional<Cart> availableService = cartList.stream().filter(c -> c.getServiceName().equalsIgnoreCase(beautyServiceArrayList.get(position).getServices().get("name"))).findFirst();
                if (availableService.isPresent()) {
                    cart = availableService.get();
                    cart.setQuantity(cart.getQuantity() - 1);
                    beautyServiceArrayList.get(position).getServices().put("quantity", String.valueOf(cart.getQuantity()));
                } else {
                    cart.setPrice(beautyServiceArrayList.get(position).getServices().get("price"));
                    cart.setServiceName(beautyServiceArrayList.get(position).getServices().get("name"));
                   /* cart.setResourceId(Integer.parseInt(beautyServiceArrayList.get(position).getServices().get("resourceId")));*/
                    beautyServiceArrayList.get(position).getServices().put("quantity", String.valueOf(Integer.parseInt(beautyServiceArrayList.get(position).getServices().get("quantity")) - 1));
                    cart.setQuantity(Integer.parseInt(beautyServiceArrayList.get(position).getServices().get("quantity")));
                }
                return cart;
            }


        });
        return convertView;
    }


    public void hide(View view) {

        TextView txtView = view.findViewById(R.id.counter);
        //Toggle
        txtView.setVisibility(View.INVISIBLE);
        //If you want it only one time
        //txtView.setVisibility(View.VISIBLE);

    }

    public List<Cart> getArrayList() {
        return cartList;
    }


}
