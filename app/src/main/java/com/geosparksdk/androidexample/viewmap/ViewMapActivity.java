package com.geosparksdk.androidexample.viewmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.geosparksdk.androidexample.R;
import com.geosparksdk.androidexample.presistence.GeoConstant;
import com.geosparksdk.androidexample.presistence.LatLngLog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.geosparksdk.androidexample.R.id.map;


/**
 * Created by ekambaram on 20/06/16.
 */
public class ViewMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 102;
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onResume() {
        if (mMapFragment != null) {
            mMapFragment.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapFragment != null) {
            mMapFragment.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
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
        setContentView(R.layout.viewmap_activity);
        TextView back = findViewById(R.id.txt_back);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(map);
        back.setOnClickListener(this);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ViewMapActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void locationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (ContextCompat.checkSelfPermission(ViewMapActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ViewMapActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(ViewMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    ActivityCompat.requestPermissions(ViewMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
            } else if (ContextCompat.checkSelfPermission(ViewMapActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(false);
                buildGoogleApiClient();
            }
        } else {
            mMap.setMyLocationEnabled(false);
            buildGoogleApiClient();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(ViewMapActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            mMap.setMyLocationEnabled(false);
                            buildGoogleApiClient();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        locationPermission();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LatLngLog latLngLog = new LatLngLog(this);
            List<GeoConstant> mListViewItems = latLngLog.getList();
            if (mListViewItems.size() > 0) {
                for (int i = 0; i < mListViewItems.size(); i++) {
                    GeoConstant geoConstant = mListViewItems.get(i);
                    mMap.addMarker(new MarkerOptions()
                            .title(geoConstant.getmLat() + " " + geoConstant.getmLng() + " " + geoConstant.getmDateTime())
                            .position(new LatLng(Double.valueOf(geoConstant.getmLat()), Double.valueOf(geoConstant.getmLng()))));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(mListViewItems), 50));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            }
        }
    }

    private LatLngBounds getBounds(List<GeoConstant> list) {
        //Location bounds to group the markers
        return new LatLngBounds.Builder()
                .include(new LatLng(Double.valueOf(list.get(0).getmLat()), Double.valueOf(list.get(0).getmLng())))
                .include(new LatLng(Double.valueOf(list.get(list.size() - 1).getmLat()), Double.valueOf(list.get(list.size() - 1).getmLng())))
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
