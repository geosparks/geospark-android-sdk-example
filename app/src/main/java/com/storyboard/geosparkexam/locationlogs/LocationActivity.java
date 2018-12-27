package com.storyboard.geosparkexam.locationlogs;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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
import android.widget.ImageView;

import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.storage.Logs;

import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private LocationAdapter mAdapter;
    private LatLngReceiver mLatLngReceiver;
    private LinearLayoutManager mLinearLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_activity);
        ImageView txtBack = findViewById(R.id.img_back);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new LocationAdapter();
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
        List<Logs> listViewItems = Logs.getInstance(this).getLocationLogs();
        mAdapter.addAllItem(listViewItems);
    }

    private class LatLngReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshList();
            mLinearLayoutManager.scrollToPosition(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void locationJob(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            try {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo job = new JobInfo.Builder(1001, new ComponentName(context, GeoSparkImplicitService.class))
                        .setMinimumLatency(1000)
                        .setOverrideDeadline(2000)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                        .setPersisted(true)
                        .build();
                jobScheduler.schedule(job);
            } catch (Exception e) {

            }
        }
    }

}



