package com.storyboard.geosparkexam.tracked;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.presistence.GeosparkLog;

import java.util.List;

public class TrackedActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final int CUSTOM_CAP_IMAGE_REF_WIDTH_PX = 10;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 102;
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private LatLngReceiver mLatLngReceiver;

    @Override
    public void onResume() {
        if (mMapFragment != null) {
            mMapFragment.onResume();
        }
        if (mLatLngReceiver == null) {
            mLatLngReceiver = new LatLngReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(mLatLngReceiver, new IntentFilter("NEW-LOCATION"));
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mMapFragment != null) {
            mMapFragment.onPause();
        }
        if (mLatLngReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mLatLngReceiver);
            mLatLngReceiver = null;
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
        setContentView(R.layout.map_activity);
        TextView back = findViewById(R.id.txt_back);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        locationPermission();
    }

    private void locationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
            ActivityCompat.requestPermissions(TrackedActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
            createPolyLineMap();

        } else {
            mMap.setMyLocationEnabled(false);
            createPolyLineMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(TrackedActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(false);
                        locationPermission();
                    }
                }
                break;
        }
    }

    private void createPolyLineMap() {
        List<GeosparkLog> mListViewItems = GeosparkLog.getInstance(this).getGeoList();
        int size = mListViewItems.size();
        if (size > 0) {
            int color = Color.HSVToColor(
                    100, new float[]{224, 83.9f, 100});
            PolylineOptions line = new PolylineOptions();
            line.width(6).color(color);
            line.geodesic(true);
            //Draw polyline
            for (int i = 0; i < size; i++) {
                GeosparkLog geosparkLog = mListViewItems.get(i);
                line.add(new LatLng(Double.valueOf(geosparkLog.getmLat()), Double.valueOf(geosparkLog.getmLng())));
                mMap.addMarker(new MarkerOptions()
                        .title(geosparkLog.getmLat() + " " + geosparkLog.getmLng() + " " + geosparkLog.getmDateTime())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .position(new LatLng(Double.valueOf(geosparkLog.getmLat()), Double.valueOf(geosparkLog.getmLng()))));
            }
            //Design to Polyline
            Polyline polyline = mMap.addPolyline(line);
            polyline.setJointType(JointType.ROUND);
            polyline.setStartCap(new CustomCap(
                    bitmapDescriptorFromVector(TrackedActivity.this, R.drawable.ic_end),
                    CUSTOM_CAP_IMAGE_REF_WIDTH_PX));
            polyline.setEndCap(new CustomCap(
                    bitmapDescriptorFromVector(TrackedActivity.this, R.drawable.ic_start),
                    CUSTOM_CAP_IMAGE_REF_WIDTH_PX));
            //Move GoogleMap camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(mListViewItems.get(size - 1).getmLat()), Double.valueOf(mListViewItems.get(size - 1).getmLng()))));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private class LatLngReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mMap.clear();
            createPolyLineMap();
        }
    }
}
