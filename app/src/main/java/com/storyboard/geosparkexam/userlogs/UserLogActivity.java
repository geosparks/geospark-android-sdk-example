package com.storyboard.geosparkexam.userlogs;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.storage.Logs;

import java.util.List;

public class UserLogActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_activity);
        ImageView txtBack = findViewById(R.id.img_back);
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
        List<Logs> mListViewItems = Logs.getInstance(this).getAppLogs();
        adapter.addAllItem(mListViewItems);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



