package com.geospark.example.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.geospark.example.R;
import com.geospark.example.storage.GeoSparkPreferences;
import com.geospark.lib.GeoSpark;
import com.geospark.lib.callback.GeoSparkCallback;
import com.geospark.lib.models.GeoSparkError;
import com.geospark.lib.models.GeoSparkUser;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView snackBar;
    private ProgressBar progressBar;
    private EditText edtDescription, edtGetUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GeoSparkPreferences.isSignedIn(this)) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_login);
            progressBar = findViewById(R.id.progressbar);
            snackBar = findViewById(R.id.snackBar);
            edtDescription = findViewById(R.id.edtDescription);
            edtGetUser = findViewById(R.id.edtUserId);
            Button btnCreateUser = findViewById(R.id.btnCreateUser);
            Button btnGetUser = findViewById(R.id.btnGetUser);
            btnCreateUser.setOnClickListener(this);
            btnGetUser.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateUser:
                createUser();
                break;

            case R.id.btnGetUser:
                getUser();
                break;
        }
    }

    private void show() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hide() {
        progressBar.setVisibility(View.GONE);
    }

    private void createUser() {
        show();
        GeoSpark.createUser(edtDescription.getText().toString(), new GeoSparkCallback() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                Log.e("GeoSpark UserId ", geoSparkUser.getUserId());
                toggleEvents();
            }

            @Override
            public void onFailure(GeoSparkError error) {
                hide();
                showMsg(error.getMessage());
            }
        });
    }

    private void getUser() {
        String text = edtGetUser.getText().toString();
        if (TextUtils.isEmpty(text)) {
            showMsg("Enter userId");
        } else {
            show();
            GeoSpark.getUser(text, new GeoSparkCallback() {
                @Override
                public void onSuccess(GeoSparkUser geoSparkUser) {
                    Log.e("GeoSpark UserId ", geoSparkUser.getUserId());
                    hide();
                    toggleEvents();
                }

                @Override
                public void onFailure(GeoSparkError error) {
                    hide();
                    showMsg(error.getMessage());
                }
            });
        }
    }

    private void toggleEvents() {
        GeoSpark.toggleEvents(true, true, true, true, new GeoSparkCallback() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                toggleListener();
            }

            @Override
            public void onFailure(GeoSparkError error) {
                hide();
                showMsg(error.getMessage());
            }
        });
    }

    private void toggleListener() {
        GeoSpark.toggleListener(true, true, new GeoSparkCallback() {
            @Override
            public void onSuccess(GeoSparkUser geoSparkUser) {
                signedIn(geoSparkUser.getUserId());
                hide();
            }

            @Override
            public void onFailure(GeoSparkError error) {
                hide();
                showMsg(error.getMessage());
            }
        });
    }

    private void signedIn(String userId) {
        GeoSpark.subscribe(GeoSpark.Subscribe.LOCATION, userId);
        GeoSparkPreferences.setSignIn(this, true);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void showMsg(String msg) {
        Snackbar.make(snackBar, msg, Snackbar.LENGTH_SHORT).show();
    }
}