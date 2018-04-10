package com.storyboard.geosparkexam.userlog;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.presistence.GeosparkLog;

import java.util.List;

public class UserLogActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_activity);
        TextView txtBack = findViewById(R.id.txt_back);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        UserLogAdapter adapter = new UserLogAdapter(this);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        List<GeosparkLog> mListViewItems = GeosparkLog.getInstance(this).getUserList();
        adapter.addAllItem(mListViewItems);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



