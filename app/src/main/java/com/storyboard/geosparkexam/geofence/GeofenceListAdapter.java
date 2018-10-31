package com.storyboard.geosparkexam.geofence;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospark.lib.model.GeoSparkGeofence;
import com.storyboard.geosparkexam.R;

import java.util.ArrayList;
import java.util.List;

public class GeofenceListAdapter extends RecyclerView.Adapter {

    private List<GeoSparkGeofence> idList = new ArrayList<>();

    void addAllItem(List<GeoSparkGeofence> lst) {
        idList.clear();
        idList.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.location_item, parent, false);
        vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GeoSparkGeofence geoSparkGeofence = idList.get(position);
        ((ItemHolder) holder).mTxtLog.setText("Geofence Id:  " + geoSparkGeofence.getId());
        ((ItemHolder) holder).mTxtDate.setText("Geofence createdAt:  " + geoSparkGeofence.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtLog;
        private TextView mTxtDate;

        ItemHolder(View itemView) {
            super(itemView);
            mTxtLog = itemView.findViewById(R.id.txt_log);
            mTxtDate = itemView.findViewById(R.id.txt_datetime);
        }
    }
}
