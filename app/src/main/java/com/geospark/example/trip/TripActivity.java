package com.geospark.example.trip;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.geospark.example.R;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkTripsCallBack;
import com.geospark.lib.model.GeoSparkActiveTrips;
import com.geospark.lib.model.GeoSparkError;

import java.util.List;

public class TripActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_activity);
        ImageView txtBack = findViewById(R.id.txt_back);
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

        showProgressDialog("Loading trips...");
        GeoSpark.activeTrips(TripActivity.this, new GeoSparkTripsCallBack() {
            @Override
            public void onSuccess(List<GeoSparkActiveTrips> geoSparkActiveTrips) {
                stopProgressDialog();
                if (geoSparkActiveTrips.size() != 0) {
                    adapter.addAllItem(geoSparkActiveTrips);
                }
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
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



