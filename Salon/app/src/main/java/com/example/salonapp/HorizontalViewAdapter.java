package com.example.salonapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salonapp.dtos.SelectedItems;
import com.example.salonapp.interfaces.ItemClickListener;

import java.text.ParseException;

public class HorizontalViewAdapter  extends RecyclerView.Adapter<HorizontalViewAdapter.ViewHolder> {

    private final Context mContext;
    private final String[] times;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    SelectedItems selectedItems;
    private View lastSelectedItem;

    public HorizontalViewAdapter(Context context, String[] times, SelectedItems selectedItems) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.selectedItems = selectedItems;
        this.times = times;

    }


    @NonNull
    @Override
    public HorizontalViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_horizontal_layout_list_options, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String time = times[position];
        holder.timeView.setText(time);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return times.length;
    }


    

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView timeView;
        private ItemClickListener mClickListener;

        ViewHolder(View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            view.setSelected(true);
            selectedItems.setTime(times[getAdapterPosition()]);
            toggleBackgroundItem(view);
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
        public String getItem(int id) {
            return times[id];
        }

        // allows clicks events to be caught
        public void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }

    }
}
