package com.geosparksdk.androidexample.viewlatlng;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geosparksdk.androidexample.R;
import com.geosparksdk.androidexample.presistence.GeoConstant;

import java.util.ArrayList;
import java.util.List;


public class LatLngLogAdapter extends RecyclerView.Adapter {

    private Context mContext;
    List<GeoConstant> mGeoLog = new ArrayList<>();

    public LatLngLogAdapter(Context context) {
        this.mContext = context;
    }

    public void addAllItem(List<GeoConstant> lst) {
        mGeoLog.clear();
        mGeoLog.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.latlnglogs_activity_item, parent, false);
        vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GeoConstant geoConstant = mGeoLog.get(position);
        ((ItemHolder) holder).mTxtLat.setText(geoConstant.getmLat());
        ((ItemHolder) holder).mTxtLng.setText(geoConstant.getmLng());
        ((ItemHolder) holder).mTxtDateTime.setText(geoConstant.getmDateTime());
    }

    @Override
    public int getItemCount() {
        return mGeoLog.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtLat;
        private TextView mTxtLng;
        private TextView mTxtDateTime;

        ItemHolder(View itemView) {
            super(itemView);
            mTxtLat = itemView.findViewById(R.id.txt_lat);
            mTxtLng = itemView.findViewById(R.id.txt_lng);
            mTxtDateTime = itemView.findViewById(R.id.txt_datetime);
        }
    }
}
