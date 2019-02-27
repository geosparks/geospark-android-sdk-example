package com.storyboard.geosparkexam.currentlocation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkLocationCallback;
import com.geospark.lib.model.GeoSparkError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.util.App;

public class CurrentLocationActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 102;
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private EditText mEdtAccuracy;
    private int mAccuracy = 20;
    private String mType = GeoSpark.Type.GPS_NETWORK.toString();
    private ProgressDialog progressDialog;

    @Override
    public void onResume() {
        if (mMapFragment != null) {
            mMapFragment.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mMapFragment != null) {
            mMapFragment.onPause();
        }
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapFragment != null) {
            mMapFragment.onLowMemory();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_location_activity);
        ImageView back = findViewById(R.id.img_back);
        mEdtAccuracy = findViewById(R.id.edt_userid);
        TextView gps = findViewById(R.id.txt_gps);
        TextView gpsNetwork = findViewById(R.id.txt_gps_net);
        RelativeLayout rl_location = findViewById(R.id.rl_location);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        back.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        gpsNetwork.setOnClickListener(this);
        gps.setOnClickListener(this);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.rl_location:
                try {
                    if (mEdtAccuracy.getText().toString().trim().length() != 0) {
                        mAccuracy = Integer.valueOf(mEdtAccuracy.getText().toString());
                    }
                    locationPermission();
                } catch (Exception e) {
                }
                break;

            case R.id.txt_gps:
                mType = GeoSpark.Type.GPS.toString();
                break;

            case R.id.txt_gps_net:
                mType = GeoSpark.Type.GPS_NETWORK.toString();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);

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

    private void locationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
            ActivityCompat.requestPermissions(CurrentLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
            getCurrentLocation();

        } else {
            mMap.setMyLocationEnabled(false);
            getCurrentLocation();
        }
    }


    private void getCurrentLocation() {
        try {
            showProgressDialog("Get current location...");
            GeoSpark.Type type = Enum.valueOf(GeoSpark.Type.class, mType);
            GeoSpark.getCurrentLocation(this, type, mAccuracy, new GeoSparkLocationCallback() {
                @Override
                public void location(double latitude, double longitude, double accuracy) {
                    stopProgressDialog();
                    if (latitude != 0.0 && longitude != 0.0) {
                        mMap.addMarker(new MarkerOptions()
                                .title(latitude + " " + longitude + "Accuracy: " + accuracy)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .position(new LatLng(latitude, longitude)));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17);
                        mMap.moveCamera(cameraUpdate);
                    } else {
                        App.showToast(CurrentLocationActivity.this, "No location updated");
                    }
                }

                @Override
                public void onFailure(GeoSparkError geoSparkError) {
                    stopProgressDialog();
                    App.showToast(CurrentLocationActivity.this, geoSparkError.getErrorMessage());

                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(CurrentLocationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(false);
                        getCurrentLocation();
                    }
                }
                break;
        }
    }
}
