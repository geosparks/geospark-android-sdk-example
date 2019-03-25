package com.geospark.example;

import android.app.Application;

import com.geospark.lib.GeoSpark;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getToken();
        GeoSpark.initialize(this, "YOUR-PUBLISHABLE-KEY");
    }
}
