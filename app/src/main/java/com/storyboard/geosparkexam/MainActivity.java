package com.storyboard.geosparkexam;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
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
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkUser;
import com.storyboard.geosparkexam.geofence.CreateGeofenceActivity;
import com.storyboard.geosparkexam.locationlog.LocationLogActivity;
import com.storyboard.geosparkexam.presistence.GeosparkLog;
import com.storyboard.geosparkexam.presistence.SharedPreferenceHelper;
import com.storyboard.geosparkexam.sdksettings.SDKSettingsActivity;
import com.storyboard.geosparkexam.tracked.TrackedActivity;
import com.storyboard.geosparkexam.trip.TripActivity;
import com.storyboard.geosparkexam.userlog.UserLogActivity;
import com.storyboard.geosparkexam.util.AppUtil;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mCreateUser;
    private EditText mEdtUserID;
    private TextView mGetUser;
    private TextView mStartLocation;
    private TextView mStopLocation;
    private TextView mTrip;
    private TextView mGeofence;
    private TextView mLogout;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        GeoSpark.onResume(this);
        locationJob(this);
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
        getBatteryOptimisation(this);
        ImageView settings = findViewById(R.id.img_settings);
        mCreateUser = findViewById(R.id.textView_create);
        mEdtUserID = findViewById(R.id.edt_userid);
        mGetUser = findViewById(R.id.textView_getuser);
        mStartLocation = findViewById(R.id.textView_startlocation);
        mStopLocation = findViewById(R.id.textView_stoplocation);
        mTrip = findViewById(R.id.trip);
        mGeofence = findViewById(R.id.geofence);
        TextView viewLog = findViewById(R.id.textView_viewlog);
        TextView viewLatLng = findViewById(R.id.textView_viewlatlng);
        TextView viewMap = findViewById(R.id.textView_viewmap);
        mLogout = findViewById(R.id.textView_logout);
        settings.setOnClickListener(this);
        mCreateUser.setOnClickListener(this);
        mGetUser.setOnClickListener(this);
        mStartLocation.setOnClickListener(this);
        mStopLocation.setOnClickListener(this);
        mTrip.setOnClickListener(this);
        mGeofence.setOnClickListener(this);
        viewLog.setOnClickListener(this);
        viewLatLng.setOnClickListener(this);
        viewMap.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        checkButtonStatus();
    }

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

            case R.id.textView_startlocation:
                startTracking(MainActivity.this);
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

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void locationJob(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            try {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo job = new JobInfo.Builder(1001, new ComponentName(context, LocationService.class))
                        .setMinimumLatency(1000)
                        .setOverrideDeadline(2000)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                        .setPersisted(true)
                        .build();
                jobScheduler.schedule(job);
            } catch (Exception e) {

            }
        }
    }

    public static void getBatteryOptimisation(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent);
            }
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

    private void init() {

    }

    private void createUser() {
        GeoSpark.createUser(MainActivity.this, new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                Log.e("USER ID", geoSparkUser.getUserId());
                stopProgressDialog();
                GeosparkLog.getInstance(MainActivity.this).createLog("User created", geoSparkUser.getUserId());
                successCreateUser();
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                GeosparkLog.getInstance(MainActivity.this).createLog(geoSparkError.getErrorCode(), geoSparkError.getErrorMessage());
                failureCreateUser();
            }
        });
    }

    private void getUser(String uid) {
        GeoSpark.getUser(MainActivity.this, uid, new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                stopProgressDialog();
                GeosparkLog.getInstance(MainActivity.this).createLog("Session started", geoSparkUser.getUserId());
                successCreateUser();
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                GeosparkLog.getInstance(MainActivity.this).createLog(geoSparkError.getErrorCode(), geoSparkError.getErrorMessage());
                failureCreateUser();
                Log.e("Get User Error", geoSparkError.getErrorMessage());
            }
        });
    }

    private void startTracking(Context context) {
        if (!GeoSpark.checkGooglePlayService(context)) {
            AppUtil.showToast(this, "Update play service");
        } else if (!GeoSpark.checkPermission(context)) {
            GeoSpark.requestPermission(MainActivity.this);
        } else if (!GeoSpark.checkLocationServices(context)) {
            GeoSpark.requestLocationServices(MainActivity.this);
        } else {
            AppUtil.showToast(this, "Start Tracking");
            enableStartTracking();
            GeosparkLog.getInstance(context).createLog("Start Location tracking", "Started successfully");
            GeoSpark.startTracking(this);
        }
    }

    private void stopTracking() {
        enableStopTracking();
        AppUtil.showToast(this, "Stop Tracking");
        GeosparkLog.getInstance(MainActivity.this).createLog("Stop Location tracking", "Stopped successfully");
        GeoSpark.stopTracking(MainActivity.this);
    }

    private void trip() {
        startActivity(new Intent(this, TripActivity.class));
    }

    private void createGeofence() {
        startActivity(new Intent(this, CreateGeofenceActivity.class));
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
        GeoSpark.stopTracking(MainActivity.this);
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
                    true, true, false, false, false, false);
        }
    }

    private void successCreateUser() {
        SharedPreferenceHelper.changeButtonStatus(getApplicationContext(),
                false, false, true, true, true, true);
        checkButtonStatus();
    }

    private void failureCreateUser() {
        SharedPreferenceHelper.changeButtonStatus(getApplicationContext(),
                true, true, false, false, false, false);
        checkButtonStatus();
    }

    private void enableStartTracking() {
        SharedPreferenceHelper.trackStatus(getApplicationContext(), false, true, false, true);
        checkButtonStatus();
    }

    private void enableStopTracking() {
        SharedPreferenceHelper.trackStatus(getApplicationContext(), true, false, true, false);
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

        if (SharedPreferenceHelper.getTrip(getApplicationContext())) {
            mTrip.setEnabled(true);
            mTrip.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mTrip.setEnabled(false);
            mTrip.setBackgroundColor(getResources().getColor(R.color.grey));
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
