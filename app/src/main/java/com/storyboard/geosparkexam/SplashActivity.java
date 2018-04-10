package com.storyboard.geosparkexam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.geospark.lib.GeoSpark;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.storyboard.geosparkexam.presistence.GeosparkLog;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getToken();
        GeoSpark.initialize(this,”YOUR-SDK-KEY”,”YOUR-SECRET”);
        GeosparkLog.getInstance(this).createLog("SDK", "SDK initialized.");
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(3 * 1000);
                    GeosparkLog.getInstance(SplashActivity.this);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
}



