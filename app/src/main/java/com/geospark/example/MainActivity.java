package com.geospark.example;

import android.app.ProgressDialog;
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

import com.geospark.example.storage.GeoSparkPref;
import com.geospark.example.trip.TripActivity;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkCallBack;
import com.geospark.lib.callback.GeoSparkLogoutCallBack;
import com.geospark.lib.model.GeoSparkError;
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
        Util.locationJob(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
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

        GeoSpark.disableBatteryOptimization(this);
        checkButtonStatus();
        setUserId();
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

    private void setUserId() {
        if (GeoSparkPref.getUserId(this) != null) {
            mUserID.setText(GeoSparkPref.getUserId(this));
        }
        if (GeoSparkPref.getDescription(this) != null) {
            mDesc.setText(GeoSparkPref.getDescription(this));
        }
    }

    private void createUser() {
        GeoSpark.createUser(this, mEdtDescription.getText().toString(), new GeoSparkCallBack() {
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

    private void getUser(String userId) {
        GeoSpark.getUser(this, userId, new GeoSparkCallBack() {
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

    private void setDescription(String description) {
        GeoSpark.setDescription(this, description, new GeoSparkCallBack() {
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
                GeoSparkPref.setUserCreated(getApplicationContext());
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
        if (!GeoSparkPref.isUserCreated(getApplicationContext())) {
            GeoSparkPref.setButtonStatus(getApplicationContext(), true, true, false, false);
        }
    }

    private void successCreateUser() {
        GeoSparkPref.setButtonStatus(getApplicationContext(), false, false, true, true);
        checkButtonStatus();
    }

    private void failureCreateUser() {
        GeoSparkPref.setButtonStatus(getApplicationContext(), true, true, false, false);
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
        if (GeoSparkPref.getCreateButtonStatus(getApplicationContext()) || GeoSparkPref.getUserButtonStatus(getApplicationContext())) {
            enable(mCreateUser);
            enable(mGetUser);
            disable(mSetDescription);
            disable(mTrip);
        } else {
            disable(mCreateUser);
            disable(mGetUser);
            enable(mSetDescription);
            enable(mTrip);
        }
        if (GeoSparkPref.getStartTrackButtonStatus(getApplicationContext())) {
            enable(mStartLocation);
        } else {
            disable(mStartLocation);
        }
        if (GeoSparkPref.getStopTrackButtonStatus(getApplicationContext())) {
            enable(mStopLocation);
        } else {
            disable(mStopLocation);
        }
        if (GeoSparkPref.getLogout(getApplicationContext())) {
            enable(mLogout);
        } else {
            disable(mLogout);
        }
    }

    private void enable(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        view.setEnabled(true);
    }

    private void disable(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.grey));
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
