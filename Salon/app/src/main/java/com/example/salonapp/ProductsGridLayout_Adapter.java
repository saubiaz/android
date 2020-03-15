package com.example.salonapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salonapp.dtos.LayoutOptions;
import com.example.salonapp.dtos.ProductDetails;

import java.util.List;

public class ProductsGridLayout_Adapter  extends BaseAdapter {

    private final Context mContext;
    private final List<ProductDetails> productDetails;

    // 1
    public ProductsGridLayout_Adapter(Context context, List<ProductDetails> productDetailsList) {
        this.mContext = context;
        this.productDetails = productDetailsList;
    }

    // 2
    @Override
    public int getCount() {
        return productDetails.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ProductDetails productDetails = this.productDetails.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.activity_product__grid__layout, null);


            final ImageView imageView = convertView.findViewById(R.id.product_imageview_cover_art);
            final TextView nameTextView = convertView.findViewById(R.id.product_option_name);
            final TextView priceTextView = convertView.findViewById(R.id.product_price);
            TextView counter = convertView.findViewById(R.id.counter);

            final ProductsGridLayout_Adapter.ViewHolder viewHolder = new ProductsGridLayout_Adapter.ViewHolder(imageView, nameTextView,priceTextView);
            convertView.setTag(viewHolder);
        }


        final ProductsGridLayout_Adapter.ViewHolder viewHolder = (ProductsGridLayout_Adapter.ViewHolder) convertView.getTag();
        viewHolder.imageViewCoverArt.setImageResource(productDetails.getResourceId());
        viewHolder.nameTextView.setText(productDetails.getDescription());
        viewHolder.priceTextView.setText(String.format("%s %s", mContext.getResources().getString(R.string.rupee), productDetails.getPrice().intValue()));


        return convertView;
    }

    private class ViewHolder {

        private final ImageView imageViewCoverArt;
        private final TextView nameTextView;
        private final TextView priceTextView;

        private ViewHolder(ImageView imageViewCoverArt, TextView nameTextView, TextView priceTextView) {

            this.imageViewCoverArt = imageViewCoverArt;
            this.nameTextView = nameTextView;
            this.priceTextView = priceTextView;

        }
    }
}