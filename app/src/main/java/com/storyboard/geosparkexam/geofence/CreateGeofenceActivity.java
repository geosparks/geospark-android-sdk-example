package com.storyboard.geosparkexam.geofence;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkGeofenceCallBack;
import com.geospark.lib.model.GeoSparkError;
import com.geospark.lib.model.GeoSparkGeofence;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.presistence.GeosparkLog;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CreateGeofenceActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private ProgressDialog progressDialog;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 102;
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private EditText radius;
    private LatLng point;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geofence_activity);

        ImageView back = findViewById(R.id.img_back);
        radius = findViewById(R.id.edt_radius);
        final EditText seconds = findViewById(R.id.edt_expiry);
        TextView createGeofence = findViewById(R.id.txt_geofence);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        back.setOnClickListener(this);
        mMapFragment.getMapAsync(this);
        createGeofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radius.getText().toString().trim().length() != 0 && seconds.getText().toString().trim().length() != 0) {
                    if (point != null) {
                        showProgressDialog("Creating geofence...");
                        GeoSpark.createGeofence(CreateGeofenceActivity.this, point.latitude, point.longitude,
                                Integer.valueOf(radius.getText().toString()), Integer.valueOf(radius.getText().toString()),
                                new GeoSparkGeofenceCallBack() {
                                    @Override
                                    public void onSuccess(GeoSparkGeofence geoSparkGeofence) {
                                        stopProgressDialog();
                                        GeosparkLog.getInstance(CreateGeofenceActivity.this).createLog("Create Geofence", String.valueOf(geoSparkGeofence.getId()));
                                        Toast.makeText(CreateGeofenceActivity.this, "Geofence created " + geoSparkGeofence.getId(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(GeoSparkError geoSparkError) {
                                        stopProgressDialog();
                                        GeosparkLog.getInstance(CreateGeofenceActivity.this).createLog("Create Geofence error", geoSparkError.getErrorCode() + geoSparkError.getErrorMessage());
                                        Toast.makeText(CreateGeofenceActivity.this, "Geofence error" + geoSparkError.getErrorCode() + geoSparkError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(CreateGeofenceActivity.this, "LatLong null value.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateGeofenceActivity.this, "Enter radius or seconds.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.geofence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGeofenceActivity.this, GeofenceListActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        requestLocation();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                if (radius.getText().toString().trim().length() != 0) {
                    CreateGeofenceActivity.this.point = point;
                    mMap.addMarker(new MarkerOptions()
                            .title(String.valueOf(point.latitude + " " + point.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .position(point));
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(point);
                    circleOptions.radius(Integer.valueOf(radius.getText().toString()));
                    circleOptions.strokeColor(getResources().getColor(R.color.colorBlue));
                    circleOptions.fillColor(getResources().getColor(R.color.colorLightBlue));
                    circleOptions.strokeWidth(5);
                    mMap.addCircle(circleOptions);
                    Toast.makeText(CreateGeofenceActivity.this, point.latitude + ", " + point.longitude, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateGeofenceActivity.this, "Enter radius", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void removeLocationUpdates() {
        if (locationCallback != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            try {
                final Location location = locationResult.getLastLocation();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                mMap.animateCamera(cameraUpdate);
                removeLocationUpdates();
            } catch (Exception e) {
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(CreateGeofenceActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(false);
                        requestLocation();
                    }
                }
                break;
        }
    }

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(CreateGeofenceActivity.this);
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



