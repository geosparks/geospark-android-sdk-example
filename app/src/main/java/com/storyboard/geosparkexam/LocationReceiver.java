package com.storyboard.geosparkexam;

import android.content.Context;
import android.location.Location;
import com.geospark.lib.location.services.GeoSparkReceiver;
import com.geospark.lib.model.GeoSparkUser;
import com.storyboard.geosparkexam.presistence.GeosparkLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LocationReceiver extends GeoSparkReceiver {

    @Override
    public void onLocationUpdated(Context context, Location location, GeoSparkUser user, String activityType) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss aa");
        GeosparkLog geosparkLog = new GeosparkLog();
        geosparkLog.setmLat(String.valueOf(location.getLatitude()));
        geosparkLog.setmLng(String.valueOf(location.getLongitude()));
        geosparkLog.setmSpeed(String.valueOf(location.getSpeed() + "-Acc-" + location.getAccuracy() + "-ACT-" + activityType));
        geosparkLog.setmDateTime(sdf.format(calendar.getTime()));
        GeosparkLog.getInstance(context).createLatLngLog(geosparkLog);
    }
}

