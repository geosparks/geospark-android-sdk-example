package com.geospark.example.locationlogs;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.geospark.example.MainActivity;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GeoSparkImplicitService extends JobService {
    LocationReceiver mLocationReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationReceiver = new LocationReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        try {
            registerReceiver(mLocationReceiver, new IntentFilter("com.geospark.android.RECEIVED"));
        } catch (Exception e) {
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        try {
            unregisterReceiver(mLocationReceiver);
            MainActivity.locationJob(this);
        } catch (Exception e) {
        }
        return true;
    }
}
