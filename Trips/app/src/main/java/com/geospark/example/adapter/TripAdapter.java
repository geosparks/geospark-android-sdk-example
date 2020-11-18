package com.geospark.example.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.geospark.example.R;
import com.geospark.example.ui.TripActivity;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkDeleteTripCallback;
import com.geospark.lib.callback.GeoSparkSyncTripCallback;
import com.geospark.lib.callback.GeoSparkTripCallback;
import com.geospark.lib.models.ActiveTrips;
import com.geospark.lib.models.GeoSparkError;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ItemHolder> {

    private TripActivity activity;
    private List<ActiveTrips> lists = new ArrayList<>();

    public TripAdapter(TripActivity activity) {
        this.activity = activity;
    }

    public void addList(List<ActiveTrips> lists) {
        this.lists.clear();
        this.lists.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        final ActiveTrips activeTrips = lists.get(position);
        holder.mTxtTripId.setText(activeTrips.getTripId());
        holder.mTxtDate.setText(activeTrips.getUpdatedAt());
        if (!TextUtils.isEmpty(activeTrips.getSyncStatus())) {
            holder.mTxtSyncStatus.setText("Trip status: " + activeTrips.getSyncStatus());
        }
        if (activeTrips.getEnded()) {
            holder.mTxtStart.setTextColor(activity.getResources().getColor(R.color.colorBorder));
            holder.mTxtResume.setTextColor(activity.getResources().getColor(R.color.colorBorder));
            holder.mTxtPause.setTextColor(activity.getResources().getColor(R.color.colorBorder));
            holder.mTxtStop.setTextColor(activity.getResources().getColor(R.color.colorBorder));
            holder.mTxtForceStop.setTextColor(activity.getResources().getColor(R.color.colorBorder));
        } else if (activeTrips.isStarted()) {
            if (activeTrips.isPaused()) {
                holder.mTxtStart.setTextColor(activity.getResources().getColor(R.color.colorBorder));
                holder.mTxtResume.setTextColor(activity.getResources().getColor(R.color.colorBlack));
                holder.mTxtPause.setTextColor(activity.getResources().getColor(R.color.colorBorder));
                holder.mTxtStop.setTextColor(activity.getResources().getColor(R.color.colorBlack));
                holder.mTxtForceStop.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            } else {
                holder.mTxtStart.setTextColor(activity.getResources().getColor(R.color.colorBorder));
                holder.mTxtResume.setTextColor(activity.getResources().getColor(R.color.colorBorder));
                holder.mTxtPause.setTextColor(activity.getResources().getColor(R.color.colorBlack));
                holder.mTxtStop.setTextColor(activity.getResources().getColor(R.color.colorBlack));
                holder.mTxtForceStop.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            }
        } else {
            holder.mTxtStart.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            holder.mTxtResume.setTextColor(activity.getResources().getColor(R.color.colorBorder));
            holder.mTxtPause.setTextColor(activity.getResources().getColor(R.color.colorBorder));
            holder.mTxtStop.setTextColor(activity.getResources().getColor(R.color.colorBorder));
            holder.mTxtForceStop.setTextColor(activity.getResources().getColor(R.color.colorBorder));
        }
        holder.mTxtSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeoSpark.syncTrip(activeTrips.getTripId(), new GeoSparkSyncTripCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        removeItem(position);
                        activity.refreshList();
                    }

                    @Override
                    public void onFailure(GeoSparkError error) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        Toast.makeText(activity, "Trip Sync: " + activeTrips.getTripId() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.mTxtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showView(holder.mProgressBar);
                hideView(holder.mTxtStart);
                hideView(holder.mTxtResume);
                hideView(holder.mTxtPause);
                hideView(holder.mTxtStop);
                hideView(holder.mTxtForceStop);
                GeoSpark.deleteTrip(activeTrips.getTripId(), new GeoSparkDeleteTripCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        removeItem(position);
                        activity.refreshList();
                    }

                    @Override
                    public void onFailure(GeoSparkError error) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        Toast.makeText(activity, "Trip deleted: " + activeTrips.getTripId() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.mTxtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showView(holder.mProgressBar);
                hideView(holder.mTxtStart);
                hideView(holder.mTxtResume);
                hideView(holder.mTxtPause);
                hideView(holder.mTxtStop);
                hideView(holder.mTxtForceStop);
                GeoSpark.startTrip(activeTrips.getTripId(), null, new GeoSparkTripCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        activity.refreshList();
                    }

                    @Override
                    public void onFailure(GeoSparkError error) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        Toast.makeText(activity, "Trip started: " + activeTrips.getTripId() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.mTxtResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeoSpark.resumeTrip(activeTrips.getTripId(), new GeoSparkTripCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        activity.refreshList();
                    }

                    @Override
                    public void onFailure(GeoSparkError error) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        Toast.makeText(activity, "Trip resume: " + activeTrips.getTripId() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.mTxtPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeoSpark.pauseTrip(activeTrips.getTripId(), new GeoSparkTripCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        activity.refreshList();
                    }

                    @Override
                    public void onFailure(GeoSparkError error) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        Toast.makeText(activity, "Trip pause: " + activeTrips.getTripId() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.mTxtStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeoSpark.stopTrip(activeTrips.getTripId(), new GeoSparkTripCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        activity.refreshList();
                    }

                    @Override
                    public void onFailure(GeoSparkError error) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        Toast.makeText(activity, "Trip stop: " + activeTrips.getTripId() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.mTxtForceStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoSpark.forceStopTrip(activeTrips.getTripId(), new GeoSparkTripCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        activity.refreshList();
                    }

                    @Override
                    public void onFailure(GeoSparkError error) {
                        hideView(holder.mProgressBar);
                        showView(holder.mTxtStart);
                        showView(holder.mTxtResume);
                        showView(holder.mTxtPause);
                        showView(holder.mTxtStop);
                        showView(holder.mTxtForceStop);
                        Toast.makeText(activity, "Trip force stop: " + activeTrips.getTripId() + " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    private void removeItem(int position) {
        lists.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private TextView mTxtTripId;
        private TextView mTxtDate;
        private TextView mTxtStart;
        private TextView mTxtResume;
        private TextView mTxtPause;
        private TextView mTxtStop;
        private TextView mTxtForceStop;
        private TextView mTxtSync;
        private TextView mTxtSyncStatus;
        private TextView mTxtDelete;
        private ProgressBar mProgressBar;

        ItemHolder(View itemView) {
            super(itemView);
            mTxtTripId = itemView.findViewById(R.id.txt_trip_id);
            mTxtDate = itemView.findViewById(R.id.txt_date);
            mTxtStart = itemView.findViewById(R.id.txt_start);
            mTxtResume = itemView.findViewById(R.id.txt_resume);
            mTxtPause = itemView.findViewById(R.id.txt_pause);
            mTxtStop = itemView.findViewById(R.id.txt_stop);
            mTxtForceStop = itemView.findViewById(R.id.txt_force_stop);
            mTxtSync = itemView.findViewById(R.id.txt_sync);
            mTxtSyncStatus = itemView.findViewById(R.id.txt_sync_status);
            mTxtDelete = itemView.findViewById(R.id.txt_delete);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}