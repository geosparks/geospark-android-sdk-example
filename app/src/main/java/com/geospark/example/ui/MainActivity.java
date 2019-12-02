package com.geospark.example.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.geospark.example.R;
import com.geospark.example.Util;
import com.geospark.example.service.GSImplicitService;
import com.geospark.example.storage.GSPreferences;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkCallBack;
import com.geospark.lib.callback.GeoSparkEventsCallback;
import com.geospark.lib.callback.GeoSparkLogoutCallBack;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkEvents;
import com.geospark.lib.model.GeoSparkUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mCreateUser;
    private EditText mEdtUserID;
    private TextView mGetUser;
    private TextView mUserID, mDesc;
    private EditText mEdtDescription;
    private TextView mSetDescription;
    private TextView mStartLocation;
    private TextView mStopLocation;
    private TextView mTrip;
    private TextView mLogout;
    private ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(new Intent(this, GSImplicitService.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mCreateUser = findViewById(R.id.textView_create);
        mEdtUserID = findViewById(R.id.edt_userid);
        mEdtDescription = findViewById(R.id.edt_description);
        mSetDescription = findViewById(R.id.textView_description);
        mDesc = findViewById(R.id.txt_desc);
        mUserID = findViewById(R.id.txt_userid);
        mGetUser = findViewById(R.id.textView_getuser);
        mStartLocation = findViewById(R.id.textView_startlocation);
        mStopLocation = findViewById(R.id.textView_stoplocation);
        mTrip = findViewById(R.id.trip);
        mLogout = findViewById(R.id.textView_logout);

        mCreateUser.setOnClickListener(this);
        mGetUser.setOnClickListener(this);
        mStartLocation.setOnClickListener(this);
        mStopLocation.setOnClickListener(this);
        mTrip.setOnClickListener(this);
        mSetDescription.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        init();
    }

    private void init() {
        if (GSPreferences.getUserId(this) != null) {
            mUserID.setText(GSPreferences.getUserId(this));
        }
        if (GSPreferences.getDescription(this) != null) {
            mDesc.setText(GSPreferences.getDescription(this));
        }
        GeoSpark.disableBatteryOptimization(this);
        checkView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.textView_create:
                showProgressDialog("Creating User...");
                createUser();
                break;

            case R.id.textView_getuser:
                if (mEdtUserID.getText().toString().trim().length() != 0) {
                    showProgressDialog("Starting Session...");
                    getUser(mEdtUserID.getText().toString());
                } else {
                    Util.showToast(this, "Enter user id");
                }
                break;

            case R.id.textView_description:
                if (mEdtDescription.getText().toString().trim().length() != 0) {
                    showProgressDialog("Description...");
                    setDescription(mEdtDescription.getText().toString());
                } else {
                    Util.showToast(this, "Enter description");
                }
                break;

            case R.id.textView_startlocation:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startService(new Intent(this, GSImplicitService.class));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startTrackingQ();
                } else {
                    startTracking();
                }
                break;

            case R.id.textView_stoplocation:
                stopService(new Intent(this, GSImplicitService.class));
                stopTracking();
                break;

            case R.id.trip:
                trip();
                break;

            case R.id.textView_logout:
                logout();
                break;
        }
    }

    /*
        Quick start:
        -------------------
        Step 3: Create user
    */
    private void createUser() {
        GeoSpark.createUser(this, mEdtDescription.getText().toString(), new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                toggleEvents(geoSparkUser.getUserId());
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                failureView();
            }
        });
    }

    private void getUser(String userId) {
        GeoSpark.getUser(this, userId, new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                toggleEvents(geoSparkUser.getUserId());
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                failureView();
            }
        });
    }

    private void toggleEvents(String userId) {
        GeoSpark.toggleEvents(this, true, true, true, new GeoSparkEventsCallback() {
            @Override
            public void onSuccess(GeoSparkEvents geoSparkEvents) {
                stopProgressDialog();
                successView();
                mUserID.setText(userId);
                GSPreferences.setUserId(MainActivity.this, userId);
                Util.showToast(MainActivity.this, "User created successfully");
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                failureView();
            }
        });
    }

    private void setDescription(String description) {
        GeoSpark.setDescription(this, description, new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                stopProgressDialog();
                GSPreferences.setDescription(MainActivity.this, description);
                mDesc.setText(description);
                Util.showToast(MainActivity.this, "Description added successfully");
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
            }
        });
    }

    /*
       Quick start:
       -------------------
       Step 4: Start Location Tracking
    */
    private void startTracking() {
        if (!GeoSpark.checkLocationPermission(this)) {
            GeoSpark.requestLocationPermission(this);
        } else if (!GeoSpark.checkLocationServices(this)) {
            GeoSpark.requestLocationServices(this);
        } else {
            GeoSpark.startTracking(this);
            GSPreferences.setTrackingView(getApplicationContext(), false, true);
            checkView();
            Util.showToast(this, "Tracking Started");
        }
    }

    /*
       Quick start: If above Android 10
       -------------------
       Step 4: Start Location Tracking
    */
    private void startTrackingQ() {
        if (!GeoSpark.checkActivityPermission(this)) {
            GeoSpark.requestActivityPermission(this);
        } else if (!GeoSpark.checkLocationPermission(this)) {
            GeoSpark.requestLocationPermission(this);
        } else if (!GeoSpark.checkBackgroundLocationPermission(this)) {
            GeoSpark.requestBackgroundLocationPermission(this);
        } else if (!GeoSpark.checkLocationServices(this)) {
            GeoSpark.requestLocationServices(this);
        } else {
            GeoSpark.startTracking(this);
            GSPreferences.setTrackingView(getApplicationContext(), false, true);
            checkView();
            Util.showToast(this, "Tracking Started");
        }
    }

    private void stopTracking() {
        GeoSpark.stopTracking(this);
        GSPreferences.setTrackingView(getApplicationContext(), true, false);
        checkView();
        Util.showToast(this, "Tracking Stopped");
    }

    private void trip() {
        startActivity(new Intent(this, TripActivity.class));
    }

    private void logout() {
        showProgressDialog("Logging out...");
        GeoSpark.logout(this, new GeoSparkLogoutCallBack() {
            @Override
            public void onSuccess(String message) {
                stopProgressDialog();
                GSPreferences.setInit(getApplicationContext());
                initView();
                checkView();
                mUserID.setText(null);
                mDesc.setText(null);
                GSPreferences.removeItem(MainActivity.this, GSPreferences.USER_ID);
                GSPreferences.removeItem(MainActivity.this, GSPreferences.DESCRIPTION);
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
            }
        });
    }

    private void initView() {
        if (!GSPreferences.isInitialized(getApplicationContext())) {
            GSPreferences.setViewStatus(getApplicationContext(), true, true, false, false);
        }
    }

    private void successView() {
        GSPreferences.setViewStatus(getApplicationContext(), false, false, true, true);
        checkView();
    }

    private void failureView() {
        GSPreferences.setViewStatus(getApplicationContext(), true, true, false, false);
        checkView();
    }

    private void checkView() {
        if (GSPreferences.isCreateViewEnabled(getApplicationContext()) || GSPreferences.isGetUserViewEnabled(getApplicationContext())) {
            enableView(mCreateUser);
            enableView(mGetUser);
            disableView(mSetDescription);
            disableView(mTrip);
        } else {
            disableView(mCreateUser);
            disableView(mGetUser);
            enableView(mSetDescription);
            enableView(mTrip);
        }
        if (GSPreferences.isStartTrackingEnabled(getApplicationContext())) {
            enableView(mStartLocation);
        } else {
            disableView(mStartLocation);
        }
        if (GSPreferences.isStopTrackingEnabled(getApplicationContext())) {
            enableView(mStopLocation);
        } else {
            disableView(mStopLocation);
        }
        if (GSPreferences.isLogoutViewEnabled(getApplicationContext())) {
            enableView(mLogout);
        } else {
            disableView(mLogout);
        }
    }

    private void enableView(View view) {
        view.setBackgroundResource(R.drawable.rounded_bg_enable);
        view.setEnabled(true);
    }

    private void disableView(View view) {
        view.setBackgroundResource(R.drawable.rounded_bg_disable);
        view.setEnabled(false);
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
}