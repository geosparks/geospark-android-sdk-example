package com.geosparksdk.androidexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geosparksdk.androidexample.presistence.LatLngLog;
import com.geosparksdk.androidexample.presistence.SharedPreferenceHelper;
import com.geosparksdk.androidexample.presistence.ViewLogs;
import com.geosparksdk.androidexample.viewlatlng.LatLngLogActivity;
import com.geosparksdk.androidexample.viewlogs.ViewLogActivity;
import com.geosparksdk.androidexample.viewmap.ViewMapActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.storyboard.geospark.core.GeoSpark;
import com.storyboard.geospark.model.ErrorResponse;
import com.storyboard.geospark.model.GeoSparkCallBack;
import com.storyboard.geospark.model.SuccessResponse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mCreateUser;
    private EditText mEdtUserID;
    private TextView mGetUser;
    private TextView mStartLocation;
    private TextView mStopLocation;
    private TextView mLogout;
    private TextView mViewLog;
    private TextView mViewLatLng;
    private TextView mViewMap;
    private ViewLogs mViewLogs;
    private LatLngLog mLatLngLog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getToken();
        GeoSpark.initialize(this,"YOUR-SDK-KEY","YOUR-SECRET");
        initButtonStatus();
        mCreateUser = (TextView) findViewById(R.id.textView_create);
        mEdtUserID = (EditText) findViewById(R.id.edt_userid);
        mGetUser = (TextView) findViewById(R.id.textView_getuser);
        mStartLocation = (TextView) findViewById(R.id.textView_startlocation);
        mStopLocation = (TextView) findViewById(R.id.textView_stoplocation);
        mViewLog = (TextView) findViewById(R.id.textView_viewlog);
        mViewLatLng = (TextView) findViewById(R.id.textView_viewlatlng);
        mViewMap = (TextView) findViewById(R.id.textView_viewmap);
        mLogout = (TextView) findViewById(R.id.textView_logout);

        mViewLogs = new ViewLogs(this);
        mLatLngLog = new LatLngLog(this);
        mViewLogs.createLog("SDK init", "SDK initialized.");
        checkButtonStatus();
        showProgressDialog();

        mCreateUser.setOnClickListener(this);
        mGetUser.setOnClickListener(this);
        mStartLocation.setOnClickListener(this);
        mStopLocation.setOnClickListener(this);
        mViewLog.setOnClickListener(this);
        mViewLatLng.setOnClickListener(this);
        mViewMap.setOnClickListener(this);
        mLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_create:
                if (progressDialog != null) {
                    progressDialog.show();
                }
                createUser();
                break;

            case R.id.textView_getuser:
                if (progressDialog != null) {
                    progressDialog.show();
                }
                getUser();
                break;

            case R.id.textView_startlocation:
                startTracking();
                break;

            case R.id.textView_stoplocation:
                stopTracking();
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
        }
    }

    private void createUser() {
        String deviceToken = SharedPreferenceHelper.getStringText(getApplicationContext(), "DEVICE_TOKEN");
        GeoSpark.createUser(MainActivity.this, deviceToken, new GeoSparkCallBack() {
            @Override
            public void success(SuccessResponse successResponse) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (successResponse.getUserID() != null) {
                    SharedPreferenceHelper.setStringText(getApplicationContext(), "USERID", successResponse.getUserID());
                    mViewLogs.createLog("User created", successResponse.getUserID());
                    successCreateUser();
                }
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (errorResponse.getErrorMessage() != null) {
                    mViewLogs.createLog("User created error create funtion", errorResponse.getErrorMessage());
                    failureCreateUser();
                }
            }
        });
    }

    private void getUser() {
        String uid;
        if (mEdtUserID.getText().toString().length() != 0) {
            uid = mEdtUserID.getText().toString();
        } else {
            uid = SharedPreferenceHelper.getStringText(getApplicationContext(), "USERID");
        }
        try {
            String deviceToken = SharedPreferenceHelper.getStringText(getApplicationContext(), "DEVICE_TOKEN");
            GeoSpark.getUser(MainActivity.this, uid, deviceToken, new GeoSparkCallBack() {
                @Override
                public void success(SuccessResponse successResponse) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (successResponse.getUserID() != null) {
                        SharedPreferenceHelper.setStringText(getApplicationContext(), "USERID", successResponse.getUserID());
                        mViewLogs.createLog("User created", successResponse.getUserID());
                        successCreateUser();
                    }
                }

                @Override
                public void failure(ErrorResponse errorResponse) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    mViewLogs.createLog("User created error get funtion", errorResponse.getErrorMessage());
                    failureCreateUser();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTracking() {
        if (!GeoSpark.checkGooglePlayService(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "Update play service", Toast.LENGTH_SHORT).show();
        } else if (!GeoSpark.checkPermission(MainActivity.this)) {
            GeoSpark.requestPermission(MainActivity.this);
        } else if (!GeoSpark.checkLocationServices(MainActivity.this)) {
            GeoSpark.requestLocationServices(MainActivity.this);
        } else {
            enableStartTracking();
            mViewLogs.createLog("Start tracking", "Started successfully");
            GeoSpark.startLocationTracking(MainActivity.this);
        }
    }

    private void stopTracking() {
        enableStopTracking();
        mViewLogs.createLog("Stop tracking", "Stopped successfully");
        GeoSpark.stopLocationTracking(MainActivity.this);
    }

    private void viewLogs() {
        startActivity(new Intent(MainActivity.this, ViewLogActivity.class));
    }

    private void viewLatLngLogs() {
        startActivity(new Intent(MainActivity.this, LatLngLogActivity.class));
    }

    private void viewMap() {
        startActivity(new Intent(MainActivity.this, ViewMapActivity.class));
    }

    private void logout() {
        GeoSpark.stopLocationTracking(MainActivity.this);
        SharedPreferenceHelper.clearInfo(MainActivity.this, "USERID");
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "INIT", false);
        mViewLogs.clearMainTable();
        mLatLngLog.clearMainTable();
        initButtonStatus();
        checkButtonStatus();
    }

    private void initButtonStatus() {
        boolean init = SharedPreferenceHelper.getBoolean(getApplicationContext(), "INIT");
        if (!init) {
            SharedPreferenceHelper.setBoolean(getApplicationContext(), "CRAETEUSER", true);
            SharedPreferenceHelper.setBoolean(getApplicationContext(), "GETUSER", true);
            SharedPreferenceHelper.setBoolean(getApplicationContext(), "STARTTRACK", false);
            SharedPreferenceHelper.setBoolean(getApplicationContext(), "STOPTRACK", false);
            SharedPreferenceHelper.setBoolean(getApplicationContext(), "LOGOUT", false);
            SharedPreferenceHelper.setBoolean(getApplicationContext(), "INIT", true);
        }
    }

    private void successCreateUser() {
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "CRAETEUSER", false);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "GETUSER", false);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "STARTTRACK", true);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "STOPTRACK", false);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "LOGOUT", true);
        checkButtonStatus();
    }

    private void failureCreateUser() {
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "CRAETEUSER", true);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "GETUSER", true);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "STARTTRACK", false);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "STOPTRACK", false);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "LOGOUT", false);
        checkButtonStatus();
    }

    private void enableStartTracking() {
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "STARTTRACK", false);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "STOPTRACK", true);
        checkButtonStatus();
    }

    private void enableStopTracking() {
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "STARTTRACK", true);
        SharedPreferenceHelper.setBoolean(getApplicationContext(), "STOPTRACK", false);
        checkButtonStatus();
    }

    private void checkButtonStatus() {
        boolean createUser = SharedPreferenceHelper.getBoolean(getApplicationContext(), "CRAETEUSER");
        boolean getUser = SharedPreferenceHelper.getBoolean(getApplicationContext(), "GETUSER");
        boolean startTracking = SharedPreferenceHelper.getBoolean(getApplicationContext(), "STARTTRACK");
        boolean stopTracking = SharedPreferenceHelper.getBoolean(getApplicationContext(), "STOPTRACK");
        boolean logout = SharedPreferenceHelper.getBoolean(getApplicationContext(), "LOGOUT");

        if (createUser) {
            mCreateUser.setEnabled(true);
            mGetUser.setEnabled(true);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mCreateUser.setEnabled(false);
            mGetUser.setEnabled(false);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        if (getUser) {
            mCreateUser.setEnabled(true);
            mGetUser.setEnabled(true);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mCreateUser.setEnabled(false);
            mGetUser.setEnabled(false);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        if (startTracking) {
            mStartLocation.setEnabled(true);
            mStartLocation.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mStartLocation.setEnabled(false);
            mStartLocation.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        if (stopTracking) {
            mStopLocation.setEnabled(true);
            mStopLocation.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mStopLocation.setEnabled(false);
            mStopLocation.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        if (logout) {
            mLogout.setEnabled(true);
            mLogout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mLogout.setEnabled(false);
            mLogout.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(true);
    }
}
