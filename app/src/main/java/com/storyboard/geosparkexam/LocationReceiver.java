package com.storyboard.geosparkexam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.storyboard.geosparkexam.presistence.GeosparkLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("com.geospark.action.LOCATION")) {
            //Lat and Lng
            String lat = intent.getStringExtra("LATITUDE");
            String lng = intent.getStringExtra("LONGITUDE");
            String speed = intent.getStringExtra("SPEED");
            //Date & Time
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss aa");
            GeosparkLog geosparkLog = new GeosparkLog();
            geosparkLog.setmLng(lng);
            geosparkLog.setmLat(lat);
            geosparkLog.setmSpeed(speed);
            geosparkLog.setmDateTime(sdf.format(calendar.getTime()));
            GeosparkLog.getInstance(context).createLatLngLog(geosparkLog);
            Intent intent1 = new Intent("NEW-LOCATION");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
        }
    }
}

