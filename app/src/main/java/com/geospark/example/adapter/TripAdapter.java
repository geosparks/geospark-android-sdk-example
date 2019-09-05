package com.geospark.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geospark.example.R;
import com.geospark.example.Util;
import com.geospark.example.ui.TripActivity;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkTripCallBack;
import com.geospark.lib.model.GeoSparkActiveTrips;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkTrip;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ItemHolder> {

    private TripActivity mActivity;
    private List<GeoSparkActiveTrips> mTripList = new ArrayList<>();

    public TripAdapter(TripActivity activity) {
        this.mActivity = activity;
    }

    public void addAllItem(List<GeoSparkActiveTrips> lst) {
        mTripList.clear();
        mTripList.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final GeoSparkActiveTrips item = mTripList.get(position);
        holder.mTxtId.setText(item.getTripId());
        try {
            holder.mTxtDate.setText(Util.utcToLocal(item.getUpdatedAt()));
        } catch (ParseException e) {
        }
        if (item.isStarted()) {
            hideView(holder.mTxtStart);
            showView(holder.mTxtStop);
            holder.mTxtStop.setText("End trip");
        } else {
            showView(holder.mTxtStart);
            hideView(holder.mTxtStop);
            holder.mTxtStart.setText("Start trip");
        }
        holder.mTxtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideView(holder.mTxtStart);
                hideView(holder.mTxtStop);
                showView(holder.mProgressBar);
                if (!GeoSpark.checkLocationPermission(mActivity)) {
                    GeoSpark.requestLocationPermission(mActivity);
                } else if (!GeoSpark.checkLocationServices(mActivity)) {
                    GeoSpark.requestLocationServices(mActivity);
                } else {
                    GeoSpark.startTrip(mActivity, item.getTripId(), null, new GeoSparkTripCallBack() {
                        @Override
                        public void onSuccess(GeoSparkTrip geoSparkTrip) {
                            success(" Trip started ");
                            hideView(holder.mProgressBar);
                            mActivity.getActiveTrips();
                        }

                        @Override
                        public void onFailure(GeoSparkError geoSparkError) {
                            hideView(holder.mProgressBar);
                            showView(holder.mTxtStart);
                            hideView(holder.mTxtStop);
                            failure(geoSparkError);
                        }
                    });
                }
            }
        });

        holder.mTxtStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideView(holder.mTxtStart);
                hideView(holder.mTxtStop);
                showView(holder.mProgressBar);
                if (!GeoSpark.checkLocationPermission(mActivity)) {
                    GeoSpark.requestLocationPermission(mActivity);
                } else if (!GeoSpark.checkLocationServices(mActivity)) {
                    GeoSpark.requestLocationServices(mActivity);
                } else {
                    GeoSpark.endTrip(mActivity, item.getTripId(), new GeoSparkTripCallBack() {

                        @Override
                        public void onSuccess(GeoSparkTrip geoSparkTrip) {
                            success(" Trip ended ");
                            hideView(holder.mProgressBar);
                            removeItem(position);
                            mActivity.getActiveTrips();
                        }

                        @Override
                        public void onFailure(GeoSparkError geoSparkError) {
                            hideView(holder.mProgressBar);
                            showView(holder.mTxtStart);
                            showView(holder.mTxtStop);
                            failure(geoSparkError);
                        }
                    });
                }
            }
        });
    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    private void success(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    private void failure(GeoSparkError geoSparkError) {
        Toast.makeText(mActivity, geoSparkError.getErrorCode() + " " + geoSparkError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    private void removeItem(int position) {
        mTripList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTripList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

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