package com.geospark.example.service;

import android.content.Context;
import android.util.Log;

import com.geospark.lib.models.GeoSparkError;
import com.geospark.lib.models.GeoSparkLocation;
import com.geospark.lib.service.GeoSparkReceiver;

public class LocationReceiver extends GeoSparkReceiver {

    @Override
    public void onLocationUpdated(Context context, GeoSparkLocation geoSparkLocation) {
        super.onLocationUpdated(context, geoSparkLocation);
        Log.e("Location", "Lat " + geoSparkLocation.getLocation().getLatitude() + " Lng " + geoSparkLocation.getLocation().getLongitude());
    }

    @Override
    public void onError(Context context, GeoSparkError geoSparkError) {
        Log.e("Location", geoSparkError.getMessage());
    }
}

