package com.storyboard.geosparkexam;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;

import com.geospark.lib.model.GeoSparkUser;
import com.geospark.lib.services.receiver.GeoSparkReceiver;
import com.storyboard.geosparkexam.presistence.GeosparkLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LocationReceiver extends GeoSparkReceiver {
    @Override
    public void onLocationUpdated(Context context, Location location, GeoSparkUser geoSparkUser) {
        //Lat and Lng
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss aa");
        GeosparkLog geosparkLog = new GeosparkLog();
        geosparkLog.setmLat(String.valueOf(location.getLatitude()));
        geosparkLog.setmLng(String.valueOf(location.getLongitude()));
        geosparkLog.setmSpeed(String.valueOf(location.getSpeed()));
        geosparkLog.setmDateTime(sdf.format(calendar.getTime()));
        GeosparkLog.getInstance(context).createLatLngLog(geosparkLog);
        Intent intent1 = new Intent("NEW-LOCATION");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
    }
}

