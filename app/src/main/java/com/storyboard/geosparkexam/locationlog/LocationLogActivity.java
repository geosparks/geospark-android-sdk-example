package com.storyboard.geosparkexam.locationlog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.presistence.GeosparkLog;

import java.util.List;

public class LocationLogActivity extends AppCompatActivity {

    private LocationLogAdapter mAdapter;
    private LatLngReceiver mLatLngReceiver;
    private LinearLayoutManager mLinearLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_activity);
        TextView txtBack = findViewById(R.id.txt_back);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new LocationLogAdapter();
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        refreshList();
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        if (mLatLngReceiver == null) {
            mLatLngReceiver = new LatLngReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(mLatLngReceiver, new IntentFilter("NEW-LOCATION"));
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mLatLngReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mLatLngReceiver);
            mLatLngReceiver = null;
        }
        super.onPause();
    }

    private void refreshList() {
        List<GeosparkLog> listViewItems = GeosparkLog.getInstance(this).getGeoReverseList();
        mAdapter.addAllItem(listViewItems);
    }

    private class LatLngReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshList();
            mLinearLayoutManager.scrollToPosition(0);
        }
    }

}



