package com.geospark.example.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.geospark.example.R;
import com.geospark.example.service.ForegroundService;
import com.geospark.example.storage.GeoSparkPreferences;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.GeoSparkTrackingMode;
import com.geospark.lib.callback.GeoSparkCreateTripCallback;
import com.geospark.lib.callback.GeoSparkLogoutCallback;
import com.geospark.lib.models.GeoSparkError;
import com.geospark.lib.models.createtrip.GeoSparkCreateTrip;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private CheckBox ckOffline;
    private RadioGroup mRadioGroup;
    private Button btnStartTracking, btnStopTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GeoSpark.disableBatteryOptimization();

        progressBar = findViewById(R.id.progressbar);
        mRadioGroup = findViewById(R.id.radioGroup);
        btnStartTracking = findViewById(R.id.btnStartTracking);
        btnStopTracking = findViewById(R.id.btnStopTracking);
        ckOffline = findViewById(R.id.ckOffline);
        Button btnCreateTrip = findViewById(R.id.btnCreateTrip);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnTrip = findViewById(R.id.btnTrip);

        btnStartTracking.setOnClickListener(this);
        btnStopTracking.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnTrip.setOnClickListener(this);
        btnCreateTrip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTracking:
                tracking();
                break;
            case R.id.btnStopTracking:
                stopTracking();
                break;
            case R.id.btnCreateTrip:
                createTrip();
                break;
            case R.id.btnTrip:
                startActivity(new Intent(this, TripActivity.class).putExtra("OFFLINE", ckOffline.isChecked()));
                break;
            case R.id.btnLogout:
                logout();
                break;
        }
    }

    private void show() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hide() {
        progressBar.setVisibility(View.GONE);
    }

    private void createTrip() {
        show();
        GeoSpark.createTrip(null, null, ckOffline.isChecked(), new GeoSparkCreateTripCallback() {
            @Override
            public void onSuccess(GeoSparkCreateTrip geoSparkCreateTrip) {
                hide();
            }

            @Override
            public void onFailure(GeoSparkError status) {
                hide();
            }
        });
    }

    private void tracking() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionsQ();
        } else {
            checkPermissions();
        }
    }

    private void checkPermissions() {
        if (!GeoSpark.checkLocationPermission()) {
            GeoSpark.requestLocationPermission(this);
        } else if (!GeoSpark.checkLocationServices()) {
            GeoSpark.requestLocationServices(this);
        } else {
            startTracking();
        }
    }

    private void checkPermissionsQ() {
        if (!GeoSpark.checkLocationPermission()) {
            GeoSpark.requestLocationPermission(this);
        } else if (!GeoSpark.checkBackgroundLocationPermission()) {
            GeoSpark.requestBackgroundLocationPermission(this);
        } else if (!GeoSpark.checkLocationServices()) {
            GeoSpark.requestLocationServices(this);
        } else {
            startTracking();
        }
    }

    private void startTracking() {
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.rbOption1) {
            GeoSpark.startTracking(GeoSparkTrackingMode.ACTIVE);
            trackingStatus();
        } else if (selectedId == R.id.rbOption2) {
            GeoSpark.startTracking(GeoSparkTrackingMode.REACTIVE);
            trackingStatus();
        } else if (selectedId == R.id.rbOption3) {
            GeoSpark.startTracking(GeoSparkTrackingMode.PASSIVE);
            trackingStatus();
        } else if (selectedId == R.id.rbOption4) {
            GeoSparkTrackingMode geoSparkTrackingMode = new GeoSparkTrackingMode.Builder(30)
                    .setDesiredAccuracy(GeoSparkTrackingMode.DesiredAccuracy.HIGH)
                    .build();
            GeoSpark.startTracking(geoSparkTrackingMode);
            trackingStatus();
        } else if (selectedId == R.id.rbOption5) {
            GeoSparkTrackingMode geoSparkTrackingMode = new GeoSparkTrackingMode.Builder(100, 60)
                    .setDesiredAccuracy(GeoSparkTrackingMode.DesiredAccuracy.HIGH)
                    .build();
            GeoSpark.startTracking(geoSparkTrackingMode);
            trackingStatus();
        } else {
            Toast.makeText(this, "Select tracking optons", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopTracking() {
        GeoSpark.stopTracking();
        trackingStatus();
    }

    private void trackingStatus() {
        if (GeoSpark.isLocationTracking()) {
            startService(new Intent(this, ForegroundService.class));
            btnStartTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            btnStopTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_enable));
            btnStartTracking.setEnabled(false);
            btnStopTracking.setEnabled(true);
        } else {
            stopService(new Intent(this, ForegroundService.class));
            btnStartTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_enable));
            btnStopTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            btnStartTracking.setEnabled(true);
            btnStopTracking.setEnabled(false);
        }
    }

    private void logout() {
        GeoSpark.logout(new GeoSparkLogoutCallback() {
            @Override
            public void onSuccess(String message) {
                GeoSparkPreferences.setSignIn(getApplicationContext(), false);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(GeoSparkError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GeoSpark.REQUEST_CODE_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tracking();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
                }
                break;
            case GeoSpark.REQUEST_CODE_BACKGROUND_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tracking();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Background Location permission required", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GeoSpark.REQUEST_CODE_LOCATION_ENABLED) {
            tracking();
        }
    }
}