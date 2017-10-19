package com.geosparksdk.androidexample.viewlogs;

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
import com.geosparksdk.androidexample.presistence.ViewLogs;

import java.util.ArrayList;
import java.util.List;

public class ViewLogActivity extends AppCompatActivity {

    private TextView mTxtBack;
    private RecyclerView mRecyclerView;
    private ViewLogAdapter mAdapter;
    private List<GeoConstant> mListViewItems = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_activity);
        mTxtBack = (TextView) findViewById(R.id.txt_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new ViewLogAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ViewLogs logDAO = new ViewLogs(this);
        mListViewItems = logDAO.getList();
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



