package com.geospark.example;

import android.app.Application;

import com.geospark.lib.GeoSpark;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GeoSpark.initialize(this, "YOUR-PUBLISHABLE-KEY");
    }
}
