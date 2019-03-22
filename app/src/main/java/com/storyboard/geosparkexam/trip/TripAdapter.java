package com.storyboard.geosparkexam.trip;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkTripCallBack;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkTrip;
import com.geospark.lib.model.GeoSparkTrips;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.storage.Logs;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private List<GeoSparkTrips> mGeoLog = new ArrayList<>();

    TripAdapter(Activity activity) {
        this.mActivity = activity;
    }

    void addAllItem(List<GeoSparkTrips> lst) {
        mGeoLog.clear();
        mGeoLog.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GeoSparkTrips geoSparkTrips = mGeoLog.get(position);
        ((ItemHolder) holder).mTxtId.setText(geoSparkTrips.getTripId());
        if (geoSparkTrips.getDescription() != null) {
            ((ItemHolder) holder).mTxtDesp.setText(geoSparkTrips.getDescription());
        }
        ((ItemHolder) holder).mTxtDate.setText(geoSparkTrips.getTripStartedAt());
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
                    GeoSpark.endTrip(mActivity, geoSparkTrips.getTripId(), new GeoSparkTripCallBack() {

                        @Override
                        public void onSuccess(GeoSparkTrip geoSparkTrip) {
                            ((ItemHolder) holder).mProgressBar.setVisibility(View.GONE);
                            ((ItemHolder) holder).mTxtStop.setVisibility(View.VISIBLE);
                            Logs.getInstance(mActivity).applicationLog("Trip ended", geoSparkTrips.getTripId());
                            Toast.makeText(mActivity, "Trip successfully ended.", Toast.LENGTH_SHORT).show();
                            removeItem(position);
                        }

                        @Override
                        public void onFailure(GeoSparkError geoSparkError) {
                            ((ItemHolder) holder).mProgressBar.setVisibility(View.GONE);
                            ((ItemHolder) holder).mTxtStop.setVisibility(View.VISIBLE);
                            Logs.getInstance(mActivity).applicationLog("Trip ended error", geoSparkTrips.getTripId() + " " + geoSparkError.getErrorCode() + " " + geoSparkError.getErrorMessage());
                            Toast.makeText(mActivity, geoSparkError.getErrorCode() + " " + geoSparkError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void removeItem(int position) {
        mGeoLog.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mGeoLog.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private TextView mTxtId;
        private TextView mTxtDate;
        private TextView mTxtStop;
        private TextView mTxtDesp;
        private ProgressBar mProgressBar;

        ItemHolder(View itemView) {
            super(itemView);
            mTxtId = itemView.findViewById(R.id.txt_id);
            mTxtDate = itemView.findViewById(R.id.txt_date);
            mTxtStop = itemView.findViewById(R.id.txt_stop);
            mTxtDesp = itemView.findViewById(R.id.txt_desp);
            mProgressBar = itemView.findViewById(R.id.pb);
        }
    }
}
