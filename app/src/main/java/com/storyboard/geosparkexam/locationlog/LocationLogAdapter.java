package com.storyboard.geosparkexam.locationlog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.presistence.GeosparkLog;

import java.util.ArrayList;
import java.util.List;

public class LocationLogAdapter extends RecyclerView.Adapter {

    private List<GeosparkLog> mGeoLog = new ArrayList<>();

    void addAllItem(List<GeosparkLog> lst) {
        mGeoLog.clear();
        mGeoLog.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.location_activity_item, parent, false);
        vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GeosparkLog geosparkLog = mGeoLog.get(position);
        ((ItemHolder) holder).mTxtLog.setText("Lat:  " + geosparkLog.getmLat() + " \nLng:  " + geosparkLog.getmLng() + " \nSpeed:  " + geosparkLog.getmSpeed());
        ((ItemHolder) holder).mTxtDateTime.setText("Date&Time: " + geosparkLog.getmDateTime());
    }

    @Override
    public int getItemCount() {
        return mGeoLog.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtLog;
        private TextView mTxtDateTime;

        ItemHolder(View itemView) {
            super(itemView);
            mTxtLog = itemView.findViewById(R.id.txt_log);
            mTxtDateTime = itemView.findViewById(R.id.txt_datetime);
        }
    }
}
