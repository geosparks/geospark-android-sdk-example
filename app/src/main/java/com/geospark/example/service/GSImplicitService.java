package com.geospark.example.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

public class GSImplicitService extends Service {
    private LocationReceiver mLocationReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        register();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    private void register() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLocationReceiver = new LocationReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.geospark.android.RECEIVED");
            registerReceiver(mLocationReceiver, intentFilter);
        }
    }

    private void unRegister() {
        if (mLocationReceiver != null) {
            unregisterReceiver(mLocationReceiver);
        }
    }
}