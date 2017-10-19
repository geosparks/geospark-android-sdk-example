package com.geosparksdk.androidexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RestrictTo;

import com.geosparksdk.androidexample.presistence.GeoConstant;
import com.geosparksdk.androidexample.presistence.LatLngLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

@RestrictTo(LIBRARY)
public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("intent.geospark.location")) {
            //Lat and Lng
            String lat = intent.getStringExtra("LATITUDE");
            String lng = intent.getStringExtra("LONGITUDE");
            //Date & Time
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa");
            LatLngLog latLngLog = new LatLngLog(context);
            GeoConstant geoConstant = new GeoConstant();
            geoConstant.setmLng(lng);
            geoConstant.setmLat(lat);
            geoConstant.setmDateTime(sdf.format(calendar.getTime()));
            latLngLog.createLatLngLog(geoConstant);
        }
    }
}

