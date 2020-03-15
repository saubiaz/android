package com.example.salonapp;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.salonapp.ExpandableServicesFragment.OnListFragmentInteractionListener;
import com.example.salonapp.dtos.BeautyService;
import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dummy.DummyContent.DummyItem;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ExpandableServicesFgmntRecyclerViewAdapter extends RecyclerView.Adapter<ExpandableServicesFgmntRecyclerViewAdapter.ViewHolder> {

    private final List<BeautyService> beautyServiceList;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    int mCartItemCount;
    List<Cart> cartList;

    public ExpandableServicesFgmntRecyclerViewAdapter(ArrayList<BeautyService> items, OnListFragmentInteractionListener listener, Context applicationContext, List<Cart> cartItems, int cartCount) {
        beautyServiceList = items;
        mListener = listener;
        context = applicationContext;
        cartList = cartItems;
        mCartItemCount = cartCount;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pedicure_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int beautyServiceArrayListPosition) {
        holder.mItem = beautyServiceList.get(beautyServiceArrayListPosition);
        holder.serviceName.setText(beautyServiceList.get(beautyServiceArrayListPosition).getServices().get("name"));
        holder.desc.setText(beautyServiceList.get(beautyServiceArrayListPosition).getServices().get("description"));
        holder.points.setText("Ella Beauty Points :"+ beautyServiceList.get(beautyServiceArrayListPosition).getServices().get("points"));
        holder.price.setText(context.getResources().getString(R.string.rupee) + " " + beautyServiceList.get(beautyServiceArrayListPosition).getServices().get("price"));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.

                   // mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.incrementButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                public void onClick(View v) {
                //createCart
               Cart cart = getSelectedListItem(holder.getAdapterPosition(), cartList);

                holder.counter.setText(cart.getQuantity().toString());
                if (null == cartList) new ArrayList<>();
                if (!cartList.contains(cart)) {
                    cartList.add(cart);
                }
                //incrementButton.setVisibility(View.INVISIBLE);
                if (cart.getQuantity() >= 0) {
                    holder.counter.setVisibility(View.VISIBLE);
                    holder.decrementButton.setVisibility(View.VISIBLE);
                }
                mCartItemCount++;
                mListener.onListFragmentInteraction(mCartItemCount,cartList);

            }

            private Cart getSelectedListItem(int position, List<Cart> cartList) {
                Cart cart;
                Optional<Cart> availableService = cartList.stream().filter(c -> c.getServiceName().equalsIgnoreCase(beautyServiceList.get(position).getServices().get("name"))).findFirst();
                if (availableService.isPresent()) {
                    cart = availableService.get();
                    cart.setQuantity(cart.getQuantity() + 1);
                    beautyServiceList.get(position).getServices().put("quantity", String.valueOf(cart.getQuantity()));
                } else {
                    cart = new Cart();
                    cart.setServiceId(beautyServiceList.get(position).getServices().get("serviceId"));
                    cart.setPrice(beautyServiceList.get(position).getServices().get("price"));
                    cart.setServiceName(beautyServiceList.get(position).getServices().get("name"));
                    /*cart.setResourceId(Integer.parseInt(beautyServiceArrayList.get(position).getServices().get("resourceId")));*/
                    if (beautyServiceList.get(position).getServices().containsKey("quantity")) {
                        beautyServiceList.get(position).getServices().put("quantity", String.valueOf(Integer.parseInt(beautyServiceList.get(position).getServices().get("quantity")) + 1));
                    } else {
                        beautyServiceList.get(position).getServices().put("quantity", "1");
                    }
                    cart.setQuantity(Integer.parseInt(beautyServiceList.get(position).getServices().get("quantity")));
                }
                return cart;
            }
        });


        holder. decrementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cart cart = null;

                if (!CollectionUtils.isEmpty(cartList)) {
                    cart = getSelectedListItem(v, cartList);
                    holder.counter.setText(cart.getQuantity().toString());

                }
                if (cart.getQuantity() <= 0) {
                    holder.counter.setVisibility(View.INVISIBLE);
                    holder.decrementButton.setVisibility(View.INVISIBLE);
                    cartList.remove(cart);
                }
                // incrementButton.setVisibility(View.VISIBLE);
                mCartItemCount--;
                mListener.onListFragmentInteraction(mCartItemCount,cartList);
                //adapterCallback.onChangeBadgeCount(mCartItemCount);
            }

            private Cart getSelectedListItem(View v, List<Cart> cartList) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                Cart cart = new Cart();
                final int position = listView.getPositionForView(parentRow);
                //  cart.setServiceId(beautyServiceArrayList.get(position).getServices().get("serviceId"));
                Optional<Cart> availableService = cartList.stream().filter(c -> c.getServiceName().equalsIgnoreCase(beautyServiceList.get(position).getServices().get("name"))).findFirst();
                if (availableService.isPresent()) {
                    cart = availableService.get();
                    cart.setQuantity(cart.getQuantity() - 1);
                    beautyServiceList.get(position).getServices().put("quantity", String.valueOf(cart.getQuantity()));
                } else {
                    cart.setPrice(beautyServiceList.get(position).getServices().get("price"));
                    cart.setServiceName(beautyServiceList.get(position).getServices().get("name"));
                    /* cart.setResourceId(Integer.parseInt(beautyServiceArrayList.get(position).getServices().get("resourceId")));*/
                    beautyServiceList.get(position).getServices().put("quantity", String.valueOf(Integer.parseInt(beautyServiceList.get(position).getServices().get("quantity")) - 1));
                    cart.setQuantity(Integer.parseInt(beautyServiceList.get(position).getServices().get("quantity")));
                }
                return cart;
            }


        });
    }

    @Override
    public int getItemCount() {
        return beautyServiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView serviceName;
        public TextView desc;
        public TextView points;
        public TextView price;
        public TextView counter;
        Button incrementButton;
        Button decrementButton;
        public BeautyService mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            serviceName =  view.findViewById(R.id.service_name);
            desc = view.findViewById(R.id.description);
            points = view.findViewById(R.id.points);
            price = view.findViewById(R.id.price) ;
            counter = view.findViewById(R.id.counter) ;
            incrementButton= view.findViewById(R.id.increment);
            decrementButton= view.findViewById(R.id.decrement);

        }

    }
}
