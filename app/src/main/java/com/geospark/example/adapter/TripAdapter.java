package com.geospark.example.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geospark.example.R;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkTripCallBack;
import com.geospark.lib.model.GeoSparkActiveTrips;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkTrip;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private List<GeoSparkActiveTrips> mActiveTrips = new ArrayList<>();

    public TripAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void addAll(List<GeoSparkActiveTrips> lst) {
        mActiveTrips.clear();
        mActiveTrips.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final GeoSparkActiveTrips geoSparkActiveTrips = mActiveTrips.get(position);
        ((ItemHolder) holder).mTxtId.setText(geoSparkActiveTrips.getTripId());
        ((ItemHolder) holder).mTxtDate.setText(geoSparkActiveTrips.getUpdatedAt());
        if (geoSparkActiveTrips.getIsStarted()) {
            ((ItemHolder) holder).mTxtStart.setVisibility(View.GONE);
            ((ItemHolder) holder).mTxtStop.setVisibility(View.VISIBLE);
            ((ItemHolder) holder).mTxtStop.setText("End trip");
        } else {
            ((ItemHolder) holder).mTxtStop.setVisibility(View.GONE);
            ((ItemHolder) holder).mTxtStart.setVisibility(View.VISIBLE);
            ((ItemHolder) holder).mTxtStart.setText("Start trip");
        }
        ((ItemHolder) holder).mTxtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ItemHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((ItemHolder) holder).mTxtStart.setVisibility(View.GONE);
                if (!GeoSpark.checkLocationPermission(mActivity)) {
                    GeoSpark.requestLocationPermission(mActivity);
                } else if (!GeoSpark.checkLocationServices(mActivity)) {
                    GeoSpark.requestLocationServices(mActivity);
                } else {
                    GeoSpark.startTrip(mActivity, geoSparkActiveTrips.getTripId(), null, new GeoSparkTripCallBack() {
                        @Override
                        public void onSuccess(GeoSparkTrip geoSparkTrip) {
                            ((ItemHolder) holder).mProgressBar.setVisibility(View.GONE);
                            ((ItemHolder) holder).mTxtStart.setVisibility(View.GONE);
                            ((ItemHolder) holder).mTxtStop.setVisibility(View.VISIBLE);
                            ((ItemHolder) holder).mTxtStop.setText("End trip");
                            Toast.makeText(mActivity, "Trip successfully started.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(GeoSparkError geoSparkError) {
                            ((ItemHolder) holder).mProgressBar.setVisibility(View.GONE);
                            ((ItemHolder) holder).mTxtStart.setVisibility(View.VISIBLE);
                            Toast.makeText(mActivity, geoSparkError.getErrorCode() + " " + geoSparkError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        ((ItemHolder) holder).mTxtStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ItemHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((ItemHolder) holder).mTxtStop.setVisibility(View.GONE);
                if (!GeoSpark.checkLocationPermission(mActivity)) {
                    GeoSpark.requestLocationPermission(mActivity);
                } else if (!GeoSpark.checkLocationServices(mActivity)) {
                    GeoSpark.requestLocationServices(mActivity);
                } else {
                    GeoSpark.endTrip(mActivity, geoSparkActiveTrips.getTripId(), new GeoSparkTripCallBack() {

                        @Override
                        public void onSuccess(GeoSparkTrip geoSparkTrip) {
                            ((ItemHolder) holder).mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mActivity, "Trip successfully ended.", Toast.LENGTH_SHORT).show();
                            remove(position);
                        }

                        @Override
                        public void onFailure(GeoSparkError geoSparkError) {
                            ((ItemHolder) holder).mProgressBar.setVisibility(View.GONE);
                            ((ItemHolder) holder).mTxtStop.setVisibility(View.VISIBLE);
                            Toast.makeText(mActivity, geoSparkError.getErrorCode() + " " + geoSparkError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void remove(int position) {
        mActiveTrips.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mActiveTrips.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private TextView mTxtId;
        private TextView mTxtDate;
        private TextView mTxtStart;
        private TextView mTxtStop;
        private ProgressBar mProgressBar;

        ItemHolder(View itemView) {
            super(itemView);
            mTxtId = itemView.findViewById(R.id.txt_id);
            mTxtDate = itemView.findViewById(R.id.txt_date);
            mTxtStart = itemView.findViewById(R.id.txt_start);
            mTxtStop = itemView.findViewById(R.id.txt_stop);
            mProgressBar = itemView.findViewById(R.id.pb);
        }
    }
}