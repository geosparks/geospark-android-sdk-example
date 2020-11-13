package com.geospark.example;

import android.app.Application;

import com.geospark.lib.GeoSpark;


public class MainApplication extends Application {
    /*
        Quick start:
        -------------------
        Step 2: Initialize SDK
    */
    @Override
    public void onCreate() {
        super.onCreate();
        GeoSpark.initialize(this, "1c46d881b9c505aa6446e97ed4be079a1844e6ec866af58f53675f4d18cb5385");
    }
}
