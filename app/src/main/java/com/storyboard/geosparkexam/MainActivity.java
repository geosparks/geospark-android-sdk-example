package com.storyboard.geosparkexam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.ErrorResponse;
import com.geospark.lib.callback.GeoSparkCallBack;
import com.geospark.lib.callback.SuccessResponse;
import com.storyboard.geosparkexam.locationlog.LocationLogActivity;
import com.storyboard.geosparkexam.presistence.GeosparkLog;
import com.storyboard.geosparkexam.presistence.SharedPreferenceHelper;
import com.storyboard.geosparkexam.sdksettings.SDKSettingsActivity;
import com.storyboard.geosparkexam.tracked.TrackedActivity;
import com.storyboard.geosparkexam.userlog.UserLogActivity;
import com.storyboard.geosparkexam.util.AppUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mVersion;
    private TextView mCreateUser;
    private EditText mEdtUserID;
    private EditText mEdtDescription;
    private TextView mGetUser;
    private TextView mStartLocation;
    private TextView mStopLocation;
    private TextView mStartMock;
    private TextView mStopMock;
    private TextView mLogout;
    private ProgressDialog progressDialog;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_settings:
                startActivity(new Intent(MainActivity.this, SDKSettingsActivity.class));
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
                    AppUtil.showToast(this, "Enter user id");
                }
                break;

            case R.id.textView_desc:
                if (mEdtDescription.getText().toString().trim().length() != 0) {
                    GeoSpark.setDescription(this, mEdtDescription.getText().toString());
                } else {
                    AppUtil.showToast(this, "Enter description");
                }
                break;

            case R.id.textView_startlocation:
                startTracking();
                break;

            case R.id.textView_stoplocation:
                stopTracking();
                break;

            case R.id.textView_startmock:
                startMock();
                break;

            case R.id.textView_stopmock:
                stopMock();
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

    @Override
    protected void onResume() {
        super.onResume();
        GeoSpark.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GeoSpark.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GeoSpark.onDestroy(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        AppUtil.getBatteryOptimisation(this);
        initButtonStatus();
        ImageView settings = findViewById(R.id.img_settings);
        mVersion = findViewById(R.id.textView_version);
        mCreateUser = findViewById(R.id.textView_create);
        mEdtUserID = findViewById(R.id.edt_userid);
        mEdtDescription = findViewById(R.id.edt_description);
        mGetUser = findViewById(R.id.textView_getuser);

        mStartLocation = findViewById(R.id.textView_startlocation);
        mStopLocation = findViewById(R.id.textView_stoplocation);
        mStartMock = findViewById(R.id.textView_startmock);
        mStopMock = findViewById(R.id.textView_stopmock);

        TextView desc = findViewById(R.id.textView_desc);
        TextView viewLog = findViewById(R.id.textView_viewlog);
        TextView viewLatLng = findViewById(R.id.textView_viewlatlng);
        TextView viewMap = findViewById(R.id.textView_viewmap);
        mLogout = findViewById(R.id.textView_logout);

        settings.setOnClickListener(this);
        mCreateUser.setOnClickListener(this);
        mGetUser.setOnClickListener(this);
        mStartLocation.setOnClickListener(this);
        mStopLocation.setOnClickListener(this);
        mStartMock.setOnClickListener(this);
        mStopMock.setOnClickListener(this);
        desc.setOnClickListener(this);
        viewLog.setOnClickListener(this);
        viewLatLng.setOnClickListener(this);
        viewMap.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        checkButtonStatus();
        showVersion();
    }

    private void showVersion() {
        mVersion.setText(AppUtil.getVersion(this));
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
        GeoSpark.createUser(MainActivity.this, new GeoSparkCallBack() {
            @Override
            public void success(SuccessResponse successResponse) {
                stopProgressDialog();
                if (successResponse.getUserID() != null) {
                    GeosparkLog.getInstance(MainActivity.this).createLog("User created", successResponse.getUserID());
                    successCreateUser();
                }
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                stopProgressDialog();
                if (errorResponse.getErrorMessage() != null) {
                    GeosparkLog.getInstance(MainActivity.this).createLog("User created error create funtion", errorResponse.getErrorMessage());
                    failureCreateUser();
                }
            }
        });
    }

    private void getUser(String uid) {
        GeoSpark.getUser(MainActivity.this, uid, new GeoSparkCallBack() {
            @Override
            public void success(SuccessResponse successResponse) {
                stopProgressDialog();
                if (successResponse.getUserID() != null) {
                    GeosparkLog.getInstance(MainActivity.this).createLog("User created", successResponse.getUserID());
                    successCreateUser();
                }
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                stopProgressDialog();
                GeosparkLog.getInstance(MainActivity.this).createLog("User created error get funtion", errorResponse.getErrorMessage());
                failureCreateUser();
            }
        });
    }

    private void startTracking() {
        if (!GeoSpark.checkGooglePlayService(MainActivity.this)) {
            AppUtil.showToast(this, "Update play service");
        } else if (!GeoSpark.checkPermission(MainActivity.this)) {
            GeoSpark.requestPermission(MainActivity.this);
        } else if (!GeoSpark.checkLocationServices(MainActivity.this)) {
            GeoSpark.requestLocationServices(MainActivity.this);
        } else {
            AppUtil.showToast(this, "Start Tracking");
            enableStartTracking();
            GeosparkLog.getInstance(MainActivity.this).createLog("Start tracking", "Started successfully");
            GeoSpark.startLocationTracking(this);
        }
    }

    private void stopTracking() {
        enableStopTracking();
        GeosparkLog.getInstance(MainActivity.this).createLog("Stop tracking", "Stopped successfully");
        GeoSpark.stopLocationTracking(MainActivity.this);
        AppUtil.showToast(this, "Stop Tracking");
    }

    private void startMock() {
        if (!GeoSpark.checkGooglePlayService(MainActivity.this)) {
            AppUtil.showToast(this, "Update play service");
        } else if (!GeoSpark.checkPermission(MainActivity.this)) {
            GeoSpark.requestPermission(MainActivity.this);
        } else if (!GeoSpark.checkLocationServices(MainActivity.this)) {
            GeoSpark.requestLocationServices(MainActivity.this);
        } else {
            enableMockStartTracking();
            GeoSpark.startMockLocationTracking(MainActivity.this);
        }
    }

    private void stopMock() {
        enableMockStopTracking();
        GeoSpark.stopMockLocationTracking(MainActivity.this);
    }

    private void viewLogs() {
        startActivity(new Intent(MainActivity.this, UserLogActivity.class));
    }

    private void viewLatLngLogs() {
        startActivity(new Intent(MainActivity.this, LocationLogActivity.class));
    }

    private void viewMap() {
        startActivity(new Intent(MainActivity.this, TrackedActivity.class));
    }

    private void logout() {
        showProgressDialog("Logging out...");
        GeoSpark.stopLocationTracking(MainActivity.this);
        SharedPreferenceHelper.saveInit(getApplicationContext());
        GeosparkLog.getInstance(MainActivity.this).clearUserTable();
        GeosparkLog.getInstance(MainActivity.this).clearGeoTable();
        initButtonStatus();
        checkButtonStatus();
        stopProgressDialog();
    }

    private void initButtonStatus() {
        if (!SharedPreferenceHelper.getInit(getApplicationContext())) {
            SharedPreferenceHelper.changeButtonStatus(getApplicationContext(),
                    true, true, false, false, false);
        }
    }

    private void successCreateUser() {
        SharedPreferenceHelper.changeButtonStatus(getApplicationContext(),
                false, false, true, true, true);
        checkButtonStatus();
    }

    private void failureCreateUser() {
        SharedPreferenceHelper.changeButtonStatus(getApplicationContext(),
                true, true, false, false, false);
        checkButtonStatus();
    }

    private void enableStartTracking() {
        SharedPreferenceHelper.trackStatus(getApplicationContext(), false, true, false);
        checkButtonStatus();
    }

    private void enableStopTracking() {
        SharedPreferenceHelper.trackStatus(getApplicationContext(), true, false, true);
        checkButtonStatus();
    }

    private void enableMockStartTracking() {
        SharedPreferenceHelper.trackMockStatus(getApplicationContext(), false, true, false);
        checkButtonStatus();
    }

    private void enableMockStopTracking() {
        SharedPreferenceHelper.trackMockStatus(getApplicationContext(), true, false, true);
        checkButtonStatus();
    }

    private void checkButtonStatus() {
        if (SharedPreferenceHelper.getCreateUser(getApplicationContext())) {
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

        if (SharedPreferenceHelper.getUser(getApplicationContext())) {
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

        if (SharedPreferenceHelper.getStartTrack(getApplicationContext())) {
            mStartLocation.setEnabled(true);
            mStartLocation.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mStartLocation.setEnabled(false);
            mStartLocation.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        if (SharedPreferenceHelper.getStopTrack(getApplicationContext())) {
            mStopLocation.setEnabled(true);
            mStopLocation.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mStopLocation.setEnabled(false);
            mStopLocation.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        if (SharedPreferenceHelper.getMockStartTrack(getApplicationContext())) {
            mStartMock.setEnabled(true);
            mStartMock.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mStartMock.setEnabled(false);
            mStartMock.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        if (SharedPreferenceHelper.getMockStopTrack(getApplicationContext())) {
            mStopMock.setEnabled(true);
            mStopMock.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mStopMock.setEnabled(false);
            mStopMock.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        if (SharedPreferenceHelper.getLogout(getApplicationContext())) {
            mLogout.setEnabled(true);
            mLogout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mLogout.setEnabled(false);
            mLogout.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }
}
