package com.geospark.example.locationlogs;

import android.content.Context;
import android.location.Location;

import com.geospark.lib.location.services.GeoSparkReceiver;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkUser;
import com.geospark.example.Util;

public class LocationReceiver extends GeoSparkReceiver {

    @Override
    public void onLocationUpdated(Context context, Location location, GeoSparkUser user, String activityType) {
        Util.showToast(context, "Location Lat:" + String.valueOf(location.getLatitude() + " Lng: " + location.getLongitude()));
    }

    @Override
    public void onError(Context context, GeoSparkError geoSparkError) {
        Util.showToast(context, geoSparkError.getErrorMessage());
    }
}

