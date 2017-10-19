package com.geosparksdk.androidexample.viewlatlng;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.geosparksdk.androidexample.R;
import com.geosparksdk.androidexample.presistence.GeoConstant;
import com.geosparksdk.androidexample.presistence.LatLngLog;

import java.util.ArrayList;
import java.util.List;

public class LatLngLogActivity extends AppCompatActivity {

    private TextView mTxtBack;
    private RecyclerView mRecyclerView;
    private LatLngLogAdapter mAdapter;
    private List<GeoConstant> mListViewItems = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_activity);
        mTxtBack = (TextView) findViewById(R.id.txt_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new LatLngLogAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        LatLngLog latLngLog = new LatLngLog(this);
        mListViewItems = latLngLog.getList();
        mAdapter.addAllItem(mListViewItems);
        mTxtBack.setOnClickListener(new View.OnClickListener() {
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
}



