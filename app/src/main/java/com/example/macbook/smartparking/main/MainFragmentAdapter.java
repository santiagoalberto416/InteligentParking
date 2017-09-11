package com.example.macbook.smartparking.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.data.sensorInfo.Sensor;

import java.util.List;

/**
 * Created by macbook on 26/06/17.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ViewHolder> {
    private List<Sensor> mDataset;
    private OnClickedItem listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView idText;
        public TextView timeText;

        public ViewHolder(View v) {
            super(v);
            idText = (TextView) v.findViewById(R.id.idText);
            timeText = (TextView) v.findViewById(R.id.timeText);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainFragmentAdapter(List<Sensor> myDataset, OnClickedItem listener) {
        mDataset = myDataset;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delay_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Sensor item = mDataset.get(position);
        holder.timeText.setText(item.getTime());
        holder.idText.setText(item.getId() + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickPosition(item.getId());
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

