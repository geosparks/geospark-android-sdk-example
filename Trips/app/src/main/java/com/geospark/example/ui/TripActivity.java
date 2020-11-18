package com.geospark.example.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geospark.example.R;
import com.geospark.example.adapter.TripAdapter;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkActiveTripsCallback;
import com.geospark.lib.models.ActiveTrips;
import com.geospark.lib.models.GeoSparkError;
import com.geospark.lib.models.GeoSparkTrip;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class TripActivity extends AppCompatActivity {
    private ProgressBar progressbar;
    private TripAdapter adapter;
    private TextView snackBar;
    private boolean offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        offline = getIntent().getBooleanExtra("OFFLINE", false);
        adapter = new TripAdapter(this);
        progressbar = findViewById(R.id.progressbar);
        snackBar = findViewById(R.id.snackBar);
        ImageView txtBack = findViewById(R.id.img_back);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        check();
    }

    private void check() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionsQ();
        } else {
            checkPermissions();
        }
    }

    private void checkPermissions() {
        if (!GeoSpark.checkLocationPermission()) {
            GeoSpark.requestLocationPermission(this);
        } else if (!GeoSpark.checkLocationServices()) {
            GeoSpark.requestLocationServices(this);
        } else {
            refreshList();
        }
    }

    private void checkPermissionsQ() {
        if (!GeoSpark.checkLocationPermission()) {
            GeoSpark.requestLocationPermission(this);
        } else if (!GeoSpark.checkBackgroundLocationPermission()) {
            GeoSpark.requestBackgroundLocationPermission(this);
        } else if (!GeoSpark.checkLocationServices()) {
            GeoSpark.requestLocationServices(this);
        } else {
            refreshList();
        }
    }

    private void show() {
        progressbar.setVisibility(View.VISIBLE);
    }

    private void hide() {
        progressbar.setVisibility(View.GONE);
    }

    public void refreshList() {
        show();
        GeoSpark.activeTrips(offline, new GeoSparkActiveTripsCallback() {
            @Override
            public void onSuccess(GeoSparkTrip geoSparkTrip) {
                hide();
                List<ActiveTrips> activeTrips = geoSparkTrip.getActiveTrips();
                if (activeTrips.size() != 0) {
                    activeTrips.get(0).getSyncStatus();
                    adapter.addList(activeTrips);
                }
            }

            @Override
            public void onFailure(GeoSparkError error) {
                hide();
            }
        });
    }

    private void showMsg(String msg) {
        Snackbar.make(snackBar, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GeoSpark.REQUEST_CODE_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    check();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showMsg("Location permission required");
                }
                break;
            case GeoSpark.REQUEST_CODE_BACKGROUND_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    check();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showMsg("Background Location permission required");
                }
                break;
            case GeoSpark.REQUEST_CODE_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    check();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showMsg("Write storage permission required");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GeoSpark.REQUEST_CODE_LOCATION_ENABLED) {
            refreshList();
        }
    }
}



