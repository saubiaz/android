package com.example.salonapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salonapp.dtos.LayoutOptions;

import java.util.List;

public class GridLayout_Adapter extends BaseAdapter {

    private final Context mContext;
    private final List<LayoutOptions> layoutOptions;

    // 1
    public GridLayout_Adapter(Context context, List<LayoutOptions> layoutOptions) {
        this.mContext = context;
        this.layoutOptions = layoutOptions;
    }

    // 2
    @Override
    public int getCount() {
        return layoutOptions.size();
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

        final LayoutOptions layoutOption = layoutOptions.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.activity_grid_layout__options, null);


        final ImageView imageView = convertView.findViewById(R.id.imageview_cover_art);
        final TextView nameTextView = convertView.findViewById(R.id.option_name);

        final ViewHolder viewHolder = new ViewHolder(imageView,nameTextView);
        convertView.setTag(viewHolder);
    }


        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.imageViewCoverArt.setImageResource(layoutOption.getResource());
        viewHolder.nameTextView.setText(mContext.getString(layoutOption.getName()));


        return convertView;
    }

    private class ViewHolder {

        private final ImageView imageViewCoverArt;
        private final TextView nameTextView;

        public ViewHolder(ImageView imageViewCoverArt, TextView nameTextView) {

            this.imageViewCoverArt = imageViewCoverArt;
            this.nameTextView = nameTextView;

        }
    }
}
