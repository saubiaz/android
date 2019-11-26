package com.example.salonapp.views;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.salonapp.R;
import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.AdapterCallBackForSelectedItems;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ListConfirmLayoutAdapter extends BaseAdapter {

    private Context context;

    AdapterCallBackForSelectedItems adapterCallBackForSelectedItems;
    private LayoutInflater inflater;
    SelectedItems selectedItems;

    public ListConfirmLayoutAdapter(Context context, SelectedItems selectedItems) {
        this.context = context;
        this.selectedItems = selectedItems;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*  this.adapterCallBackForSelectedItems = adapterCallBackForSelectedItems;*/
    }

    @Override
    public int getCount() {
        return selectedItems.getCartList().size();
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

        convertView = inflater.inflate(R.layout.activity_list_confirm_order, null);

        TextView serviceName = convertView.findViewById(R.id.service_name);

        serviceName.setText(selectedItems.getCartList().get(cartListPosition).getServiceName());

        TextView price = convertView.findViewById(R.id.price);
        price.setText(context.getResources().getString(R.string.rupee) + " " + selectedItems.getCartList().get(cartListPosition).getPrice());

        Button incrementButton = convertView.findViewById(R.id.increment);

        Button decrementButton = convertView.findViewById(R.id.decrement);

        TextView counter = convertView.findViewById(R.id.counter);
        counter.setText(String.valueOf(selectedItems.getCartList().get(cartListPosition).getQuantity()));


        incrementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //createCart
                Cart cart = getSelectedListItem(v, selectedItems.getCartList());
                counter.setText(cart.getQuantity().toString());
                if (null == selectedItems.getCartList()) new ArrayList<>();
                if (!selectedItems.getCartList().contains(cart)) {
                    selectedItems.getCartList().add(cart);
                }
                if (cart.getQuantity() >= 0) {
                    counter.setVisibility(View.VISIBLE);
                    decrementButton.setVisibility(View.VISIBLE);
                }


            }

            private Cart getSelectedListItem(View v, List<Cart> cartList) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                Cart cart;
                final int position = listView.getPositionForView(parentRow);
                Optional<Cart> availableService = cartList.stream().filter(c -> c.getServiceName().equalsIgnoreCase(selectedItems.getCartList().get(position).getServiceName())).findFirst();
                if (availableService.isPresent()) {
                    cart = availableService.get();
                    cart.setQuantity(selectedItems.getCartList().get(position).getQuantity() + 1);
                } else {
                    cart = new Cart();
                    cart.setServiceId(selectedItems.getCartList().get(position).getServiceId());
                    cart.setPrice(selectedItems.getCartList().get(position).getPrice());
                    cart.setServiceName(selectedItems.getCartList().get(position).getServiceName());
                    cart.setQuantity(selectedItems.getCartList().get(position).getQuantity() + 1);
                }
                return cart;
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cart cart = null;

                if (!CollectionUtils.isEmpty(selectedItems.getCartList())) {
                    cart = getSelectedListItem(v, selectedItems.getCartList());
                    counter.setText(cart.getQuantity().toString());

                    adapterCallBackForSelectedItems.getTheSelectedItems();
                }
                if (cart.getQuantity() <= 0) {
                    counter.setVisibility(View.INVISIBLE);
                    decrementButton.setVisibility(View.INVISIBLE);
                    selectedItems.getCartList().remove(cart);
                }
                // incrementButton.setVisibility(View.VISIBLE);
            }

            private Cart getSelectedListItem(View v, List<Cart> cartList) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                Cart cart = new Cart();
                final int position = listView.getPositionForView(parentRow);
                //  cart.setServiceId(beautyServiceArrayList.get(position).getServices().get("serviceId"));
                Optional<Cart> availableService = cartList.stream().filter(c -> c.getServiceName().equalsIgnoreCase(selectedItems.getCartList().get(position).getServiceName())).findFirst();
                if (availableService.isPresent()) {
                    cart = availableService.get();
                    cart.setQuantity(selectedItems.getCartList().get(position).getQuantity() - 1);
                }
                cart.setPrice(selectedItems.getCartList().get(position).getPrice());
                cart.setServiceName(selectedItems.getCartList().get(position).getServiceName());
                cart.setQuantity(selectedItems.getCartList().get(position).getQuantity() - 1);
                return cart;
            }
        });


        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());

                // Set a title for alert dialog
                builder.setTitle("Select your answer.");

                // Ask the final question
                builder.setMessage("Want to remove this service?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            getSelectedList(v,counter,decrementButton);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // User clicked the No button
                            Toast.makeText(context,
                                    "Your answer is no.", Toast.LENGTH_SHORT).show();
                            break;

                        case DialogInterface.BUTTON_NEUTRAL:
                            // Neutral/Cancel button clicked
                            Toast.makeText(context,
                                    "You clicked Cancel button.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", dialogClickListener);

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No", dialogClickListener);

                // Set the alert dialog cancel/neutral button click listener
                builder.setNeutralButton("Cancel", dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the three buttons alert dialog on interface
                dialog.show();
            }
        });

        return convertView;
    }

    private void getSelectedList(View view, TextView counter, View decrementButton) {
        Cart cart = null;

        if (!CollectionUtils.isEmpty(selectedItems.getCartList())) {
            cart = getSelectedListItem(view, selectedItems.getCartList());
            counter.setText(cart.getQuantity().toString());

            adapterCallBackForSelectedItems.getTheSelectedItems();
        }
        if (cart.getQuantity() <= 0) {
            counter.setVisibility(View.INVISIBLE);
            decrementButton.setVisibility(View.INVISIBLE);
            selectedItems.getCartList().remove(cart);
        }
        // incrementButton.setVisibility(View.VISIBLE);
    }

    private Cart getSelectedListItem(View v, List<Cart> cartList) {
        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        Cart cart = new Cart();
        final int position = listView.getPositionForView(parentRow);
        //  cart.setServiceId(beautyServiceArrayList.get(position).getServices().get("serviceId"));
        Optional<Cart> availableService = cartList.stream().filter(c -> c.getServiceName().equalsIgnoreCase(selectedItems.getCartList().get(position).getServiceName())).findFirst();
        if (availableService.isPresent()) {
            cart = availableService.get();
            cart.setQuantity(selectedItems.getCartList().get(position).getQuantity() - 1);
        }
        cart.setPrice(selectedItems.getCartList().get(position).getPrice());
        cart.setServiceName(selectedItems.getCartList().get(position).getServiceName());
        cart.setQuantity(selectedItems.getCartList().get(position).getQuantity() - 1);
        return cart;
    }


}
