package com.geospark.example.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.geospark.example.R;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.GeoSparkTrackingMode;
import com.geospark.lib.callback.GeoSparkCallback;
import com.geospark.lib.callback.GeoSparkCreateTripCallback;
import com.geospark.lib.callback.GeoSparkLogoutCallback;
import com.geospark.lib.models.GeoSparkError;
import com.geospark.lib.models.GeoSparkUser;
import com.geospark.lib.models.createtrip.GeoSparkCreateTrip;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView snackBar;
    private ProgressBar progressBar;
    private EditText edtDescription, edtDistanceFilter, edtGeofenceRadius, edtUpdateInterval;
    private CheckBox ckGeofence, ckTrip, ckActivity, ckNearBy, ckEvents, ckLocation,
            ckToggleEvents, ckToggleLocation;
    private RadioGroup mRadioGroup;
    private Button btnStartTracking, btnStopTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GeoSpark.disableBatteryOptimization();
        progressBar = findViewById(R.id.progressbar);
        snackBar = findViewById(R.id.snackBar);
        edtDescription = findViewById(R.id.edtDescription);
        ckGeofence = findViewById(R.id.ckGeofence);
        ckTrip = findViewById(R.id.ckTrip);
        ckActivity = findViewById(R.id.ckActivity);
        ckNearBy = findViewById(R.id.ckNearaBy);
        ckEvents = findViewById(R.id.ckEvents);
        ckLocation = findViewById(R.id.ckLocation);
        ckToggleEvents = findViewById(R.id.ckToggleEvents);
        ckToggleLocation = findViewById(R.id.ckToggleLocation);
        edtUpdateInterval = findViewById(R.id.edtUpdateInterval);
        edtGeofenceRadius = findViewById(R.id.edtGeofenceRadius);
        mRadioGroup = findViewById(R.id.radioGroup);
        Button btnDescription = findViewById(R.id.btnDescription);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnToggleEvents = findViewById(R.id.btnToggleEvents);
        Button btnGetEvents = findViewById(R.id.btnGetEvents);
        edtDistanceFilter = findViewById(R.id.edtDistanceFilter);
        btnStartTracking = findViewById(R.id.btnStartTracking);
        btnStopTracking = findViewById(R.id.btnStopTracking);
        Button btnTrip = findViewById(R.id.btnTrip);
        Button btnCreateTrip = findViewById(R.id.btnCreateTrip);

        btnDescription.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnToggleEvents.setOnClickListener(this);
        btnGetEvents.setOnClickListener(this);
        ckToggleEvents.setOnCheckedChangeListener(this);
        ckToggleLocation.setOnCheckedChangeListener(this);
        btnStartTracking.setOnClickListener(this);
        btnStopTracking.setOnClickListener(this);

        btnTrip.setOnClickListener(this);
        btnCreateTrip.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        GeoSpark.notificationOpenedHandler(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        trackingStatus();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ckToggleEvents:
                if (isChecked) {
                    GeoSpark.subscribeEvents();
                } else {
                    GeoSpark.unSubscribeEvents();
                }
                break;

            case R.id.ckToggleLocation:
                if (isChecked) {
                    GeoSpark.subscribeLocation();
                } else {
                    GeoSpark.unSubscribeLocation();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDescription:
                setDescription();
                break;
            case R.id.btnToggleEvents:
                toggleEvents();
                break;
            case R.id.btnGetEvents:
                getEvents();
                break;
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

    private void setDescription() {
        String text = edtDescription.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            showMsg("Enter description");
        } else {
            GeoSpark.setDescription(text);
        }
    }

    private void toggleEvents() {
        boolean geofence = ckGeofence.isChecked();
        boolean trip = ckTrip.isChecked();
        boolean activity = ckActivity.isChecked();
        boolean nearBy = ckNearBy.isChecked();
        final boolean events = ckEvents.isChecked();
        final boolean location = ckLocation.isChecked();
        show();
        GeoSpark.toggleEvents(geofence, trip, activity, nearBy, new GeoSparkCallback() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                GeoSpark.toggleListener(location, events, new GeoSparkCallback() {
                    @Override
                    public void onSuccess(GeoSparkUser geoSparkUser) {
                        hide();
                    }

                    @Override
                    public void onFailure(GeoSparkError error) {
                        hide();
                    }
                });
            }

            @Override
            public void onFailure(GeoSparkError error) {
                hide();
            }
        });
    }

    private void getEvents() {
        show();
        GeoSpark.getEventsStatus(new GeoSparkCallback() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                hide();
                String msg = "Geofence: " + geoSparkUser.getGeofenceEvents() +
                        " Trip: " + geoSparkUser.getTripsEvents() +
                        " Activity: " + geoSparkUser.getTripsEvents() +
                        " Events: " + geoSparkUser.getEventListenerStatus() +
                        " Location: " + geoSparkUser.getLocationListenerStatus();
                if (geoSparkUser.getGeofenceEvents()) {
                    ckGeofence.setChecked(true);
                } else {
                    ckGeofence.setChecked(false);
                }
                if (geoSparkUser.getTripsEvents()) {
                    ckTrip.setChecked(true);
                } else {
                    ckTrip.setChecked(false);
                }
                if (geoSparkUser.getLocationEvents()) {
                    ckActivity.setChecked(true);
                } else {
                    ckActivity.setChecked(false);
                }
                if (geoSparkUser.getMovingGeofenceEvents()) {
                    ckNearBy.setChecked(true);
                } else {
                    ckNearBy.setChecked(false);
                }
                if (geoSparkUser.getEventListenerStatus()) {
                    ckEvents.setChecked(true);
                } else {
                    ckEvents.setChecked(false);
                }
                if (geoSparkUser.getLocationListenerStatus()) {
                    ckLocation.setChecked(true);
                } else {
                    ckLocation.setChecked(false);
                }
            }

            @Override
            public void onFailure(GeoSparkError error) {
                hide();
                Logger.getInstance(getApplicationContext()).updateLog("Events: ", error.getMessage());
            }
        });
    }

    private void createTrip() {
        List<Double[]> origin = new ArrayList<>();
        origin.add(new Double[]{77.622977, 12.917042});
        origin.add(new Double[]{77.622977, 12.917042});
        List<Double[]> destination = new ArrayList<>();
        destination.add(new Double[]{77.650239, 12.924304});
        destination.add(new Double[]{77.697418, 12.959172});
        show();
        GeoSpark.createTrip(origin, destination, false, new GeoSparkCreateTripCallback() {
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
        if (GrantPermissions.isAbove29()) {
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
            int updateInterval;
            int distanceFilter;
            int stopDuration;
            if (TextUtils.isEmpty(edtUpdateInterval.getText().toString())) {
                updateInterval = 0;
            } else {
                updateInterval = Integer.parseInt(edtUpdateInterval.getText().toString());
            }
            if (TextUtils.isEmpty(edtDistanceFilter.getText().toString())) {
                distanceFilter = 0;
            } else {
                distanceFilter = Integer.parseInt(edtDistanceFilter.getText().toString());
            }
            if (TextUtils.isEmpty(edtGeofenceRadius.getText().toString())) {
                stopDuration = 0;
            } else {
                stopDuration = Integer.parseInt(edtGeofenceRadius.getText().toString());
            }
            if (updateInterval > 0) {
                GeoSparkTrackingMode geoSparkTrackingMode = new GeoSparkTrackingMode.Builder(updateInterval)
                        .setDesiredAccuracy(GeoSparkTrackingMode.DesiredAccuracy.HIGH)
                        .build();
                GeoSpark.startTracking(geoSparkTrackingMode);
            } else {
                GeoSparkTrackingMode geoSparkTrackingMode = new GeoSparkTrackingMode.Builder(distanceFilter, stopDuration)
                        .setDesiredAccuracy(GeoSparkTrackingMode.DesiredAccuracy.HIGH)
                        .build();
                GeoSpark.startTracking(geoSparkTrackingMode);
            }
            trackingStatus();
        } else {
            showMsg("Select tracking option");
        }
    }

    private void stopTracking() {
        GeoSpark.stopTracking();
        trackingStatus();
    }

    private void trackingStatus() {
        if (GeoSpark.isLocationTracking()) {
            ((RadioButton) mRadioGroup.getChildAt(Preferences.getRadioButtonId(this))).setChecked(true);
            startService(new Intent(this, ImplicitService.class));
            btnStartTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            btnStopTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_enable));
            btnStartTracking.setEnabled(false);
            btnStopTracking.setEnabled(true);
        } else {
            mRadioGroup.clearCheck();
            stopService(new Intent(this, ImplicitService.class));
            btnStartTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_enable));
            btnStopTracking.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            btnStartTracking.setEnabled(true);
            btnStopTracking.setEnabled(false);
        }
    }

    private void exportLog() {
        if (!GrantPermissions.checkStoragePermission(this)) {
            GrantPermissions.requestStoragePermission(this);
        } else {
            GeoSpark.exportToFile();
        }
    }

    private void logout() {
        show();
        GeoSpark.logout(new GeoSparkLogoutCallback() {
            @Override
            public void onSuccess(String message) {
                hide();
                Logger.getInstance(getApplicationContext()).clearLog();
                Preferences.setLogin(getApplicationContext(), false);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(GeoSparkError error) {
                hide();
                Logger.getInstance(getApplicationContext()).updateLog("Logout: ", error.getMessage());
            }
        });
    }

    private void showMsg(String msg) {
        Snackbar.make(snackBar, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GeoSpark.REQUEST_CODE_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tracking();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showMsg("Location permission required");
                }
                break;
            case GeoSpark.REQUEST_CODE_BACKGROUND_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tracking();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showMsg("Background Location permission required");
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