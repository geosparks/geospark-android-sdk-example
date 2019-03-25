package com.geospark.example.trip;

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

import com.geospark.example.R;
import com.geospark.example.Util;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkTripCallBack;
import com.geospark.lib.callback.GeoSparkTripsCallBack;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkTrip;
import com.geospark.lib.model.GeoSparkTrips;

import java.util.List;

public class TripActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText mEdtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_activity);

        mEdtDescription = findViewById(R.id.edt_description);
        ImageView txtBack = findViewById(R.id.txt_back);
        TextView startTrip = findViewById(R.id.textView_trip);
        TextView viewTrips = findViewById(R.id.textView_view);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
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
                if (!GeoSpark.checkLocationPermission(TripActivity.this)) {
                    GeoSpark.requestLocationPermission(TripActivity.this);
                } else if (!GeoSpark.checkLocationServices(TripActivity.this)) {
                    GeoSpark.requestLocationServices(TripActivity.this);
                } else {
                    showProgressDialog("Starting trip...");
                    GeoSpark.startTrip(TripActivity.this, mEdtDescription.getText().toString(), new GeoSparkTripCallBack() {
                        @Override
                        public void onSuccess(GeoSparkTrip geoSparkTrip) {
                            stopProgressDialog();
                            Util.showToast(TripActivity.this, "Trip created successfully.");
                        }

                        @Override
                        public void onFailure(GeoSparkError geoSparkError) {
                            stopProgressDialog();
                            Util.showToast(TripActivity.this, geoSparkError.getErrorMessage());
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
                        if (trip.size() != 0) {
                            adapter.addAllItem(trip);
                        } else {
                            Util.showToast(TripActivity.this, "No active trips.");
                        }
                    }

                    @Override
                    public void onFailure(GeoSparkError geoSparkError) {
                        stopProgressDialog();
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



