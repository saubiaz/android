package com.example.salonapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.salonapp.R;
import com.example.salonapp.dtos.Cart;
import com.example.salonapp.interfaces.AdapterCallBackForCartCount;
import com.example.salonapp.interfaces.AdapterCallBackForSelectedItems;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ListAddedServicesAdapter extends BaseAdapter {

    private Context context;

    AdapterCallBackForSelectedItems adapterCallBackForSelectedItems;
    AdapterCallBackForCartCount adapterCallBackForCartCount;
    private LayoutInflater inflater;
    public int mCartItemCount;
    boolean isService = false;
    
    List<Cart> cartList= new ArrayList();

    public ListAddedServicesAdapter(Context context,int count, List<Cart> cartList, AdapterCallBackForSelectedItems adapterCallBackForSelectedItems,
                                    AdapterCallBackForCartCount adapterCallBackForCartCount, boolean isService) {
        this.context = context;
        this.cartList= cartList;
        mCartItemCount = count;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.adapterCallBackForSelectedItems = adapterCallBackForSelectedItems;
        this.adapterCallBackForCartCount = adapterCallBackForCartCount;
        this.isService = isService;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int cartListPosition, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.activity_book_apointment_list, null);

        TextView serviceName =  convertView.findViewById(R.id.service_name);


        serviceName.setText(cartList.get(cartListPosition).getServiceName());
        ImageView imageView  = convertView.findViewById(R.id.book_img);

        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_launcher));


        TextView counter = convertView.findViewById(R.id.book_appointment_counter);
        counter.setText(String.valueOf(cartList.get(cartListPosition).getQuantity()));
        Button incrementButton = convertView.findViewById(R.id.increment);

        Button decrementButton = convertView.findViewById(R.id.decrement);

        incrementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //createCart
                Cart cart = getSelectedListItem(v,cartList);
                counter.setText(String.valueOf(cart.getQuantity()));
                if (null == cartList) new ArrayList<>();
                if(!cartList.contains(cart)) {
                    cartList.add(cart);
                }
                if(cart.getQuantity()>=0) {
                    counter.setVisibility(View.VISIBLE);
                    decrementButton.setVisibility(View.VISIBLE);
                }
                mCartItemCount++;
                adapterCallBackForCartCount.onChangeBadgeCount(mCartItemCount);
                adapterCallBackForSelectedItems.setCartList(cartList,isService);
            }

            private Cart getSelectedListItem(View v, List<Cart> cartList) {
                View parentRow = (View) v.getParent();
                CardView cardView = (CardView) parentRow.getParent();
                ListView listView = (ListView) cardView.getParent();
                Cart cart ;
                final int  position = listView.getPositionForView(parentRow);
                Optional<Cart> availableService = cartList.stream().filter(c-> c.getServiceName().equalsIgnoreCase(cartList.get(position).getServiceName())).findFirst();
                if(availableService.isPresent()){
                    cart = availableService.get();
                    cart.setQuantity(cartList.get(position).getQuantity()+1);
                }else {
                    cart = new Cart();
                    cart.setServiceId(cartList.get(position).getServiceId());
                    cart.setPrice(cartList.get(position).getPrice());
                    cart.setServiceName(cartList.get(position).getServiceName());
                    cart.setResourceId(cartList.get(position).getResourceId());
                    cart.setQuantity(cartList.get(position).getQuantity() + 1);
                }
                return cart;
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cart cart = null;

                if (!CollectionUtils.isEmpty(cartList)) {
                    cart = getSelectedListItem(v,  cartList);
                    counter.setText(cart.getQuantity().toString());
                }
                if (cart.getQuantity() <= 0) {
                    counter.setVisibility(View.INVISIBLE);
                    decrementButton.setVisibility(View.INVISIBLE);
                    cartList.remove(cart);
                }
                mCartItemCount--;
                adapterCallBackForCartCount.onChangeBadgeCount(mCartItemCount);
                adapterCallBackForSelectedItems.setCartList(cartList,isService);
                // incrementButton.setVisibility(View.VISIBLE);
            }

            private Cart getSelectedListItem(View v,List<Cart> cartList) {
                View parentRow = (View) v.getParent();
                CardView cardView = (CardView) parentRow.getParent();
                ListView listView = (ListView) cardView.getParent();
                Cart cart ;
                final int  position = listView.getPositionForView(parentRow);
                //  cart.setServiceId(beautyServiceArrayList.get(position).getServices().get("serviceId"));
                Optional<Cart> availableService = cartList.stream().filter(c -> c.getServiceName().equalsIgnoreCase(cartList.get(position).getServiceName())).findFirst();
                if (availableService.isPresent()) {
                    cart = availableService.get();
                    cart.setQuantity(cartList.get(position).getQuantity() - 1);
                }else {
                    cart=new Cart();
                    cart.setPrice(cartList.get(position).getPrice());
                    cart.setServiceName(cartList.get(position).getServiceName());
                    cart.setResourceId(cartList.get(position).getResourceId());
                    cart.setQuantity(cartList.get(position).getQuantity() - 1);
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
}
