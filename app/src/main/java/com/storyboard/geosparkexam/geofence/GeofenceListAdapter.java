package com.storyboard.geosparkexam.geofence;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkGeofenceCallBack;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkGeofence;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.storage.Logs;

import java.util.ArrayList;
import java.util.List;

public class GeofenceListAdapter extends RecyclerView.Adapter {

    private List<GeoSparkGeofence> idList = new ArrayList<>();
    private Context mContext;

    GeofenceListAdapter(Context context) {
        this.mContext = context;
    }

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
                R.layout.geofence_item, parent, false);
        vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GeoSparkGeofence geoSparkGeofence = idList.get(position);
        ((ItemHolder) holder).mTxtLog.setText("Geofence Id:  " + geoSparkGeofence.getId());
        ((ItemHolder) holder).mTxtDate.setText("Geofence createdAt:  " + geoSparkGeofence.getCreatedAt());
        ((ItemHolder) holder).mTxtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoSpark.deleteGeofence(mContext, geoSparkGeofence.getId(), new GeoSparkGeofenceCallBack() {
                    @Override
                    public void onSuccess(GeoSparkGeofence geoSparkGeofence) {
                        Logs.getInstance(mContext).applicationLog("Geofence Deleted", geoSparkGeofence.getId());
                        Toast.makeText(mContext, "Geofence successfully deleted.", Toast.LENGTH_SHORT).show();
                        removeItem(position);
                    }

                    @Override
                    public void onFailure(GeoSparkError geoSparkError) {
                        Logs.getInstance(mContext).applicationLog("Geofence delete error", geoSparkGeofence.getId() + " " + geoSparkError.getErrorCode() + " " + geoSparkError.getErrorMessage());
                        Toast.makeText(mContext, geoSparkError.getErrorCode() + " " + geoSparkError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void removeItem(int position) {
        idList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtLog;
        private TextView mTxtDate;
        private TextView mTxtDelete;

        ItemHolder(View itemView) {
            super(itemView);
            mTxtLog = itemView.findViewById(R.id.txt_log);
            mTxtDate = itemView.findViewById(R.id.txt_datetime);
            mTxtDelete = itemView.findViewById(R.id.txt_delete);
        }
    }
}
