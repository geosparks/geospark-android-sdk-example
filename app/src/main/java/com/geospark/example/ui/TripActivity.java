package com.geospark.example.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geospark.example.R;
import com.geospark.example.Util;
import com.geospark.example.adapter.TripAdapter;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkTripsCallBack;
import com.geospark.lib.model.GeoSparkActiveTrips;
import com.geospark.lib.model.GeoSparkError;

import java.util.List;

public class TripActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TripAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        mAdapter = new TripAdapter(this);
        ImageView txtBack = findViewById(R.id.txt_back);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getActiveTrips();
    }

    public void getActiveTrips() {
        showProgressDialog();
        GeoSpark.activeTrips(this, new GeoSparkTripsCallBack() {
            @Override
            public void onSuccess(List<GeoSparkActiveTrips> geoSparkActiveTrips) {
                stopProgressDialog();
                if (geoSparkActiveTrips.size() != 0) {
                    mAdapter.addAllItem(geoSparkActiveTrips);
                } else {
                    Util.showToast(TripActivity.this, "No trips available");
                }
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                Util.showToast(TripActivity.this, geoSparkError.getErrorMessage());
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(TripActivity.this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading active trips...");
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




