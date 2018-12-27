package com.storyboard.geosparkexam.geofence;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkGeofenceCallBack;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkGeofence;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.storage.Logs;

import java.util.List;

public class GeofenceListActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private GeofenceListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geofence_list_activity);
        ImageView txtBack = findViewById(R.id.img_back);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new GeofenceListAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        showProgressDialog("Loading geofence list ....");
        GeoSpark.geofenceList(this, new GeoSparkGeofenceCallBack() {
            @Override
            public void onSuccess(GeoSparkGeofence geoSparkGeofence) {
                stopProgressDialog();
                Logs.getInstance(GeofenceListActivity.this).applicationLog("Geofence Listed", String.valueOf(geoSparkGeofence.getGeofenceList().size()));
                List<GeoSparkGeofence> geoSparkGeofences = geoSparkGeofence.getGeofenceList();
                if (geoSparkGeofences.size() != 0) {
                    mAdapter.addAllItem(geoSparkGeofences);
                } else {
                    Toast.makeText(GeofenceListActivity.this, "Geofences created " + String.valueOf(geoSparkGeofence.getGeofenceList().size()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(GeoSparkError geoSparkError) {
                stopProgressDialog();
                Logs.getInstance(GeofenceListActivity.this).applicationLog("Geofence list error", geoSparkError.getErrorCode() + " " + geoSparkError.getErrorMessage());
                Toast.makeText(GeofenceListActivity.this, geoSparkError.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
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



