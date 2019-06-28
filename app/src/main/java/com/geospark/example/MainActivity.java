package com.geospark.example;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkCallBack;
import com.geospark.lib.callback.GeoSparkLogoutCallBack;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkUser;
import com.geospark.example.locationlogs.GeoSparkImplicitService;
import com.geospark.example.storage.GeoSparkPref;
import com.geospark.example.trip.TripActivity;


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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void locationJob(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            try {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo job = new JobInfo.Builder(1001, new ComponentName(context, GeoSparkImplicitService.class))
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        locationJob(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        GeoSpark.disableBatteryOptimization(this);
        initButtonStatus();

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

        checkButtonStatus();
        setUserID();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                startTracking();
                break;

            case R.id.textView_stoplocation:
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

    private void setUserID() {
        if (GeoSparkPref.getUserId(MainActivity.this) != null) {
            mUserID.setText(GeoSparkPref.getUserId(MainActivity.this));
        }
        if (GeoSparkPref.getDescription(MainActivity.this) != null) {
            mDesc.setText(GeoSparkPref.getDescription(MainActivity.this));
        }
    }

    private void createUser() {
        GeoSpark.createUser(MainActivity.this, mEdtDescription.getText().toString(), new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                if (geoSparkUser.getUserId() != null) {
                    mUserID.setText(geoSparkUser.getUserId());
                    GeoSparkPref.setUserId(MainActivity.this, geoSparkUser.getUserId());
                }
                stopProgressDialog();
                successCreateUser();
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                failureCreateUser();
            }
        });
    }

    private void setDescription(String description) {
        GeoSpark.setDescription(MainActivity.this, description, new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                stopProgressDialog();
                mDesc.setText(description);
                GeoSparkPref.setDescription(MainActivity.this, description);
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
            }
        });
    }

    private void getUser(String uid) {
        GeoSpark.getUser(MainActivity.this, uid, new GeoSparkCallBack() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                stopProgressDialog();
                if (geoSparkUser.getUserId() != null) {
                    mUserID.setText(geoSparkUser.getUserId());
                    GeoSparkPref.setUserId(MainActivity.this, geoSparkUser.getUserId());
                }
                successCreateUser();
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
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
            GeoSpark.startTracking(this);
            Util.showToast(this, "Start Tracking");
            enableStartTracking();
        }
    }

    private void stopTracking() {
        GeoSpark.stopTracking(this);
        Util.showToast(this, "Stop Tracking");
        enableStopTracking();
    }

    private void trip() {
        startActivity(new Intent(this, TripActivity.class));
    }

    private void logout() {
        showProgressDialog("Logging out...");
        GeoSpark.logout(this, new GeoSparkLogoutCallBack() {
            @Override
            public void onSuccess(String message) {
                GeoSparkPref.saveInit(getApplicationContext());
                initButtonStatus();
                checkButtonStatus();
                stopProgressDialog();
                mUserID.setText(null);
                mDesc.setText(null);
                GeoSparkPref.removeItem(MainActivity.this, "USERID");
                GeoSparkPref.removeItem(MainActivity.this, "DESCRIPTION");
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
            mTrip.setEnabled(false);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mSetDescription.setBackgroundColor(getResources().getColor(R.color.grey));
            mTrip.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            mCreateUser.setEnabled(false);
            mGetUser.setEnabled(false);
            mSetDescription.setEnabled(true);
            mTrip.setEnabled(true);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mSetDescription.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mTrip.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        if (GeoSparkPref.getUser(getApplicationContext())) {
            mCreateUser.setEnabled(true);
            mGetUser.setEnabled(true);
            mSetDescription.setEnabled(false);
            mTrip.setEnabled(false);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mSetDescription.setBackgroundColor(getResources().getColor(R.color.grey));
            mTrip.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            mCreateUser.setEnabled(false);
            mGetUser.setEnabled(false);
            mSetDescription.setEnabled(true);
            mTrip.setEnabled(true);
            mCreateUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mGetUser.setBackgroundColor(getResources().getColor(R.color.grey));
            mSetDescription.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mTrip.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
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
