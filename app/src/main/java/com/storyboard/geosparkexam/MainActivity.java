package com.storyboard.geosparkexam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkCallBack;
import com.geospark.lib.callback.GeoSparkLogoutCallBack;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkUser;
import com.storyboard.geosparkexam.currentlocation.CurrentLocationActivity;
import com.storyboard.geosparkexam.geofence.GeofenceActivity;
import com.storyboard.geosparkexam.locationlogs.LocationActivity;
import com.storyboard.geosparkexam.locationlogs.MapActivity;
import com.storyboard.geosparkexam.settings.SettingsActivity;
import com.storyboard.geosparkexam.storage.GeoSparkPref;
import com.storyboard.geosparkexam.storage.Logs;
import com.storyboard.geosparkexam.trip.TripActivity;
import com.storyboard.geosparkexam.userlogs.UserLogActivity;
import com.storyboard.geosparkexam.util.App;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mCreateUser;
    private EditText mEdtUserID;
    private TextView mGetUser;
    private EditText mEdtDescription;
    private TextView mSetDescription;
    private TextView mStartLocation;
    private TextView mStopLocation;
    private TextView mTrip;
    private TextView mGeofence;
    private TextView mViewLatLng;
    private TextView mViewMap;
    private TextView mLogout;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        LocationActivity.locationJob(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        GeoSpark.disableBatteryOptimization(this);
        GeoSpark.logger(MainActivity.this, true);
        initButtonStatus();
        ImageView settings = findViewById(R.id.img_settings);
        mCreateUser = findViewById(R.id.textView_create);
        mEdtUserID = findViewById(R.id.edt_userid);
        mEdtDescription = findViewById(R.id.edt_description);
        mSetDescription = findViewById(R.id.textView_description);
        mGetUser = findViewById(R.id.textView_getuser);
        mStartLocation = findViewById(R.id.textView_startlocation);
        mStopLocation = findViewById(R.id.textView_stoplocation);
        mTrip = findViewById(R.id.trip);
        mGeofence = findViewById(R.id.geofence);
        final TextView viewLog = findViewById(R.id.textView_viewlog);
        final TextView viewLocation = findViewById(R.id.textView_location);
        mViewLatLng = findViewById(R.id.textView_viewlatlng);
        mViewMap = findViewById(R.id.textView_viewmap);
        mLogout = findViewById(R.id.textView_logout);
        settings.setOnClickListener(this);
        mCreateUser.setOnClickListener(this);
        mGetUser.setOnClickListener(this);
        mStartLocation.setOnClickListener(this);
        mStopLocation.setOnClickListener(this);
        mTrip.setOnClickListener(this);
        mGeofence.setOnClickListener(this);
        mSetDescription.setOnClickListener(this);
        viewLog.setOnClickListener(this);
        mViewLatLng.setOnClickListener(this);
        mViewMap.setOnClickListener(this);
        viewLocation.setOnClickListener(this);
        mLogout.setOnClickListener(this);

        checkButtonStatus();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;

            case R.id.textView_create:
                showProgressDialog("Loading user...");
                createUser();
                break;

            case R.id.textView_getuser:
                if (mEdtUserID.getText().toString().trim().length() != 0) {
                    showProgressDialog("Loading user...");
                    getUser(mEdtUserID.getText().toString());
                } else {
                    App.showToast(this, "Enter user id");
                }
                break;

            case R.id.textView_description:
                if (mEdtDescription.getText().toString().trim().length() != 0) {
                    showProgressDialog("Set Description...");
                    setDescription(mEdtDescription.getText().toString());
                } else {
                    App.showToast(this, "Enter description");
                }
                break;

            case R.id.textView_startlocation:
                startTracking();
                break;

            case R.id.textView_stoplocation:
                stopTracking();
                break;

            case R.id.trip:
                trip();
                break;

            case R.id.geofence:
                createGeofence();
                break;

            case R.id.textView_viewlog:
                viewLogs();
                break;

            case R.id.textView_viewlatlng:
                viewLatLngLogs();
                break;

            case R.id.textView_viewmap:
                viewMap();
                break;

            case R.id.textView_logout:
                logout();
                break;

            case R.id.textView_location:
                startActivity(new Intent(MainActivity.this, CurrentLocationActivity.class));
                break;

        }
    }

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }

    private void stopProgressDialog() {
        if (progressDialog != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 1000);
        }
    }

    private void createUser() {
        GeoSpark.createUser(MainActivity.this, mEdtDescription.getText().toString(), new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                Log.e("USER ID", geoSparkUser.getUserId());
                stopProgressDialog();
                Logs.getInstance(MainActivity.this).applicationLog("User created", geoSparkUser.getUserId());
                successCreateUser();
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                Logs.getInstance(MainActivity.this).applicationLog(geoSparkError.getErrorCode(), geoSparkError.getErrorMessage());
                failureCreateUser();
            }
        });
    }

    private void setDescription(String description) {
        GeoSpark.setDescription(MainActivity.this, description, new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                stopProgressDialog();
                Logs.getInstance(MainActivity.this).applicationLog("Description: " + description + " ", geoSparkUser.getUserId());
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                Logs.getInstance(MainActivity.this).applicationLog(geoSparkError.getErrorCode(), geoSparkError.getErrorMessage());
            }
        });
    }

    private void getUser(String uid) {
        GeoSpark.getUser(MainActivity.this, uid, new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                stopProgressDialog();
                Logs.getInstance(MainActivity.this).applicationLog("Get User", geoSparkUser.getUserId());
                successCreateUser();
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                Logs.getInstance(MainActivity.this).applicationLog(geoSparkError.getErrorCode(), geoSparkError.getErrorMessage());
                failureCreateUser();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startTracking() {
        if (!GeoSpark.checkLocationPermission(this)) {
            GeoSpark.requestLocationPermission(this);
        } else if (!GeoSpark.checkLocationServices(this)) {
            GeoSpark.requestLocationServices(this);
        } else {
            App.showToast(this, "Start Tracking");
            enableStartTracking();
            Logs.getInstance(this).applicationLog("Start tracking", "Started successfully");
            GeoSpark.startTracking(this);
        }
    }

    private void stopTracking() {
        enableStopTracking();
        App.showToast(this, "Stop Tracking");
        Logs.getInstance(MainActivity.this).applicationLog("Stop tracking", "Stopped successfully");
        GeoSpark.stopTracking(this);
    }

    private void trip() {
        startActivity(new Intent(this, TripActivity.class));
    }

    private void createGeofence() {
        startActivity(new Intent(this, GeofenceActivity.class));
    }

    private void viewLogs() {
        startActivity(new Intent(this, UserLogActivity.class));
    }

    private void viewLatLngLogs() {
        startActivity(new Intent(this, LocationActivity.class));
    }

    private void viewMap() {
        startActivity(new Intent(this, MapActivity.class));
    }

    private void logout() {
        showProgressDialog("Logging out...");
        GeoSpark.logout(this, new GeoSparkLogoutCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.i("GeoSpark", message);
                GeoSparkPref.saveInit(getApplicationContext());
                Logs.getInstance(MainActivity.this).clearLocationLogs();
                initButtonStatus();
                checkButtonStatus();
                stopProgressDialog();
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
            }
        });
    }

    private void initButtonStatus() {
        if (!GeoSparkPref.getInit(getApplicationContext())) {
            GeoSparkPref.changeButtonStatus(getApplicationContext(), true, true, false, false);
        }
    }

    private void successCreateUser() {
        GeoSparkPref.changeButtonStatus(getApplicationContext(), false, false, true, true);
        checkButtonStatus();
    }

    private void failureCreateUser() {
        GeoSparkPref.changeButtonStatus(getApplicationContext(), true, true, false, false);
        checkButtonStatus();
    }

    private void enableStartTracking() {
        GeoSparkPref.trackStatus(getApplicationContext(), false, true);
        checkButtonStatus();
    }

    private void enableStopTracking() {
        GeoSparkPref.trackStatus(getApplicationContext(), true, false);
        checkButtonStatus();
    }

    private void checkButtonStatus() {
        if (GeoSparkPref.getCreateUser(getApplicationContext())) {
            mCreateUser.setEnabled(true);
            mGetUser.setEnabled(true);
            mSetDescription.setEnabled(false);
            mGeofence.setEnabled(false);
            mTrip.setEnabled(false);
            mViewLatLng.setEnabled(false);
            mViewMap.setEnabled(false);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mSetDescription.setBackgroundColor(getResources().getColor(R.color.grey));
            mGeofence.setBackgroundColor(getResources().getColor(R.color.grey));
            mTrip.setBackgroundColor(getResources().getColor(R.color.grey));
            mViewLatLng.setBackgroundColor(getResources().getColor(R.color.grey));
            mViewMap.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            mCreateUser.setEnabled(false);
            mGetUser.setEnabled(false);
            mSetDescription.setEnabled(true);
            mGeofence.setEnabled(true);
            mTrip.setEnabled(true);
            mViewLatLng.setEnabled(true);
            mViewMap.setEnabled(true);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mSetDescription.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mGeofence.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mTrip.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mViewLatLng.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mViewMap.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        if (GeoSparkPref.getUser(getApplicationContext())) {
            mCreateUser.setEnabled(true);
            mGetUser.setEnabled(true);
            mSetDescription.setEnabled(false);
            mGeofence.setEnabled(false);
            mTrip.setEnabled(false);
            mViewLatLng.setEnabled(false);
            mViewMap.setEnabled(false);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mSetDescription.setBackgroundColor(getResources().getColor(R.color.grey));
            mGeofence.setBackgroundColor(getResources().getColor(R.color.grey));
            mTrip.setBackgroundColor(getResources().getColor(R.color.grey));
            mViewLatLng.setBackgroundColor(getResources().getColor(R.color.grey));
            mViewMap.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            mCreateUser.setEnabled(false);
            mGetUser.setEnabled(false);
            mSetDescription.setEnabled(true);
            mGeofence.setEnabled(true);
            mTrip.setEnabled(true);
            mViewLatLng.setEnabled(true);
            mViewMap.setEnabled(true);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mSetDescription.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mGeofence.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mTrip.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mViewLatLng.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mViewMap.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        if (GeoSparkPref.getStartTrack(getApplicationContext())) {
            mStartLocation.setEnabled(true);
            mStartLocation.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mStartLocation.setEnabled(false);
            mStartLocation.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        if (GeoSparkPref.getStopTrack(getApplicationContext())) {
            mStopLocation.setEnabled(true);
            mStopLocation.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mStopLocation.setEnabled(false);
            mStopLocation.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        if (GeoSparkPref.getLogout(getApplicationContext())) {
            mLogout.setEnabled(true);
            mLogout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mLogout.setEnabled(false);
            mLogout.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }
}
