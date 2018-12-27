package com.storyboard.geosparkexam.locationlogs;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.geospark.lib.location.services.GeoSparkReceiver;
import com.geospark.lib.model.GeoSparkUser;
import com.storyboard.geosparkexam.storage.Logs;
import com.storyboard.geosparkexam.util.DateTime;

public class LocationReceiver extends GeoSparkReceiver {

    @Override
    public void onLocationUpdated(Context context, Location location, GeoSparkUser user, String activityType) {
        Log.e("LAT & LNG", String.valueOf(location.getLatitude() + "--" + location.getLongitude()));
        Logs logs = new Logs();
        logs.setmLat(String.valueOf(location.getLatitude()));
        logs.setmLng(String.valueOf(location.getLongitude()));
        logs.setmSpeed(String.valueOf(location.getSpeed()));
        logs.setmAccuracy(String.valueOf(location.getAccuracy()));
        logs.setmActivityType(activityType);
        logs.setmDateTime(DateTime.calendarDate());
        logs.setmFilterDate(DateTime.filterCalendarDate());
        Logs.getInstance(context).locationLog(logs);
    }
}

