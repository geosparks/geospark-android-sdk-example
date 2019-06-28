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
        GeoSpark.initialize(this, "bf1801d4940e700ed5b8c158dba5bac0dcc41cb0d2fb027a7fda6559857ef2bb");
    }
}
