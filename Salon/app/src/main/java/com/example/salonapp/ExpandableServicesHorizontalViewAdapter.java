package com.example.salonapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salonapp.dtos.Cart;
import com.example.salonapp.dtos.LayoutOptions;
import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.ItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ExpandableServicesHorizontalViewAdapter extends RecyclerView.Adapter<ExpandableServicesHorizontalViewAdapter.ViewHolder> {

    private final Context mContext;
    private LayoutInflater mInflater;
    private final List<LayoutOptions> layoutOptions;
    private View lastSelectedItem;
    SelectedItems selectedItems ;

    public ExpandableServicesHorizontalViewAdapter(Context context, List<LayoutOptions> layoutOptions, SelectedItems selectedItems) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.layoutOptions = layoutOptions;
        this.selectedItems = selectedItems;

    }


    @NonNull
    @Override
    public ExpandableServicesHorizontalViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_featured_products_horizontal_layout_list_options, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LayoutOptions layoutOption = layoutOptions.get(position);
        holder.featuredProduct.setText(layoutOption.getName());
        holder.imgViewIcon.setImageResource(layoutOption.getResource());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return layoutOptions.size();
    }


    

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView featuredProduct;
        ImageView imgViewIcon;
        private ItemClickListener mClickListener;

        ViewHolder(View itemView) {
            super(itemView);
            featuredProduct = itemView.findViewById(R.id.featured_product);
            imgViewIcon = itemView.findViewById(R.id.featured_product_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            LayoutOptions layoutOption = layoutOptions.get(getAdapterPosition());

            if (mContext.getString(layoutOption.getName()).equals(mContext.getString(R.string.face_skin))) {
                Intent i = new Intent(mContext, ExpandableListLayout.class);
                i.putExtra("layoutName", mContext.getString(layoutOption.getName()));
                i.putExtra("count" ,((ExpandableListLayout) mContext).adapter.mCartItemCount);
                i.putExtra("selectedItems", (Serializable) ((ExpandableListLayout) mContext).adapter.cartList);
               mContext.startActivity(i);
            }
            view.setSelected(true);
         //   toggleBackgroundItem(view);
            //onClickToAddCalendar();
        }


        private void toggleBackgroundItem(View view) {
            if (lastSelectedItem != null) {
                lastSelectedItem.setBackgroundColor(Color.TRANSPARENT);
            }
            view.setBackground(mContext.getDrawable(R.drawable.button_oval_for_time));
         //   view.setBackgroundColor(mContext.getResources().getColor(R.color.pink));
            lastSelectedItem = view;

        }



        // convenience method for getting data at click position
        public LayoutOptions getItem(int id) {
            return layoutOptions.get(id);
        }

        // allows clicks events to be caught
        public void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }

    }
}
