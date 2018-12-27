package com.storyboard.geosparkexam.trip;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkTripCallBack;
import com.geospark.lib.callback.GeoSparkTripsCallBack;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkTrip;
import com.geospark.lib.model.GeoSparkTrips;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.storage.Logs;

import java.util.List;

public class TripActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText mEdtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_activity);

        ImageView txtBack = findViewById(R.id.txt_back);
        TextView startTrip = findViewById(R.id.textView_trip);
        TextView viewTrips = findViewById(R.id.textView_view);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mEdtDescription = findViewById(R.id.edt_description);
        final TripAdapter adapter = new TripAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!GeoSpark.checkPermission(TripActivity.this)) {
                    GeoSpark.requestPermission(TripActivity.this);
                } else if (!GeoSpark.checkLocationServices(TripActivity.this)) {
                    GeoSpark.requestLocationServices(TripActivity.this);
                } else {
                    showProgressDialog("Creating trip...");
                    GeoSpark.startTrip(TripActivity.this, mEdtDescription.getText().toString(), new GeoSparkTripCallBack() {
                        @Override
                        public void onSuccess(GeoSparkTrip geoSparkTrip) {
                            stopProgressDialog();
                            Logs.getInstance(TripActivity.this).applicationLog("Trip created", geoSparkTrip.getTripId());
                            Toast.makeText(TripActivity.this, geoSparkTrip.getTripId(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(GeoSparkError geoSparkError) {
                            stopProgressDialog();
                            Logs.getInstance(TripActivity.this).applicationLog("Trip create error", geoSparkError.getErrorCode() + " " + geoSparkError.getErrorMessage());
                            Toast.makeText(TripActivity.this, geoSparkError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        viewTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog("Loading trips...");
                GeoSpark.activeTrips(TripActivity.this, new GeoSparkTripsCallBack() {
                    @Override
                    public void onSuccess(GeoSparkTrips geoSparkTrips) {
                        stopProgressDialog();
                        List<GeoSparkTrips> trip = geoSparkTrips.getTripsId();
                        Logs.getInstance(TripActivity.this).applicationLog("Active trips", "count " + String.valueOf(trip.size()));
                        if (trip.size() != 0) {
                            adapter.addAllItem(trip);
                        }
                    }

                    @Override
                    public void onFailure(GeoSparkError geoSparkError) {
                        stopProgressDialog();
                        Logs.getInstance(TripActivity.this).applicationLog("Active trips error", geoSparkError.getErrorCode() + geoSparkError.getErrorMessage());
                    }
                });
            }
        });
    }

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(TripActivity.this);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



