package com.storyboard.geosparkexam.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.geospark.lib.GeoSpark;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.storage.GeoSparkPref;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SettingsActivity extends FragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private CheckBox foreground, background, terminated, always_on;
    private CheckBox stop, walk, drive, bicycle, running, all;
    private List<GeoSpark.Type> appStateList = new ArrayList<>();
    private List<GeoSpark.Type> motionList = new ArrayList<>();

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.ckb_foreground:
                if (isChecked) {
                    appStateList.add(GeoSpark.Type.FOREGROUND);
                    appState();
                } else {
                    appStateList.remove(GeoSpark.Type.FOREGROUND);
                    removeAppStateItem(SettingsActivity.this, GeoSpark.Type.FOREGROUND.toString());
                }
                break;

            case R.id.ckb_background:
                if (isChecked) {
                    appStateList.add(GeoSpark.Type.BACKGROUND);
                    appState();
                } else {
                    appStateList.remove(GeoSpark.Type.BACKGROUND);
                    removeAppStateItem(SettingsActivity.this, GeoSpark.Type.BACKGROUND.toString());
                }
                break;

            case R.id.ckb_always_on:
                if (isChecked) {
                    appStateList.add(GeoSpark.Type.ALWAYS_ON);
                    appState();
                } else {
                    appStateList.remove(GeoSpark.Type.ALWAYS_ON);
                    removeAppStateItem(SettingsActivity.this, GeoSpark.Type.ALWAYS_ON.toString());
                }
                break;

            case R.id.ckb_stop:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.STOP);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.STOP);
                    removeMotionStateItem(SettingsActivity.this, GeoSpark.Type.STOP.toString());
                }
                break;

            case R.id.ckb_walk:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.WALK);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.WALK);
                    removeMotionStateItem(SettingsActivity.this, GeoSpark.Type.WALK.toString());
                }
                break;

            case R.id.ckb_drive:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.DRIVE);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.DRIVE);
                    removeMotionStateItem(SettingsActivity.this, GeoSpark.Type.DRIVE.toString());
                }
                break;

            case R.id.ckb_bicycle:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.BICYCLE);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.BICYCLE);
                    removeMotionStateItem(SettingsActivity.this, GeoSpark.Type.BICYCLE.toString());
                }
                break;

            case R.id.ckb_running:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.RUNNING);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.RUNNING);
                    removeMotionStateItem(SettingsActivity.this, GeoSpark.Type.RUNNING.toString());
                }
                break;

            case R.id.ckb_all:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.ALL);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.ALL);
                    removeMotionStateItem(SettingsActivity.this, GeoSpark.Type.ALL.toString());
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdk_settings_activity);
        //AppLifecycle
        ImageView back = findViewById(R.id.img_back);
        foreground = findViewById(R.id.ckb_foreground);
        background = findViewById(R.id.ckb_background);
        terminated = findViewById(R.id.ckb_terminated);
        always_on = findViewById(R.id.ckb_always_on);
        //Motion
        stop = findViewById(R.id.ckb_stop);
        walk = findViewById(R.id.ckb_walk);
        drive = findViewById(R.id.ckb_drive);
        bicycle = findViewById(R.id.ckb_bicycle);
        running = findViewById(R.id.ckb_running);
        all = findViewById(R.id.ckb_all);

        back.setOnClickListener(this);
        foreground.setOnCheckedChangeListener(this);
        background.setOnCheckedChangeListener(this);
        terminated.setOnCheckedChangeListener(this);
        always_on.setOnCheckedChangeListener(this);
        stop.setOnCheckedChangeListener(this);
        walk.setOnCheckedChangeListener(this);
        drive.setOnCheckedChangeListener(this);
        bicycle.setOnCheckedChangeListener(this);
        running.setOnCheckedChangeListener(this);
        all.setOnCheckedChangeListener(this);

        showAppStateSettings();
        showMotionStateSettings();
    }

    private void showAppStateSettings() {
        Set<String> appState = GeoSparkPref.getTrackInAppStateSettings(this);
        if (appState != null && appState.size() > 0) {
            for (String s : appState) {
                if (s.equals(GeoSpark.Type.FOREGROUND.toString())) {
                    foreground.setChecked(true);
                } else if (s.equals(GeoSpark.Type.BACKGROUND.toString())) {
                    background.setChecked(true);
                } else if (s.equals(GeoSpark.Type.ALWAYS_ON.toString())) {
                    always_on.setChecked(true);
                }
            }
        }
    }

    private void showMotionStateSettings() {
        Set<String> motionState = GeoSparkPref.getTrackingMotion(this);
        if (motionState != null && motionState.size() > 0) {
            for (String s : motionState) {
                if (s.equals(GeoSpark.Type.STOP.toString())) {
                    stop.setChecked(true);
                } else if (s.equals(GeoSpark.Type.WALK.toString())) {
                    walk.setChecked(true);
                } else if (s.equals(GeoSpark.Type.DRIVE.toString())) {
                    drive.setChecked(true);
                } else if (s.equals(GeoSpark.Type.BICYCLE.toString())) {
                    bicycle.setChecked(true);
                } else if (s.equals(GeoSpark.Type.RUNNING.toString())) {
                    running.setChecked(true);
                } else if (s.equals(GeoSpark.Type.ALL.toString())) {
                    all.setChecked(true);
                }
            }
        }
    }

    private void appState() {
        GeoSparkPref.setTrackInAppStateSettings(this, convertToStringSet(appStateList));
        GeoSpark.setTrackingInAppState(this, appStateList.toArray(new GeoSpark.Type[appStateList.size()]));
    }

    private void motionState() {
        GeoSparkPref.setTrackingMotion(this, convertToStringSet(motionList));
        GeoSpark.setTrackingInMotion(this, motionList.toArray(new GeoSpark.Type[motionList.size()]));
    }

    public static void removeAppStateItem(Context context, String value) {
        Set<String> appState = GeoSparkPref.getTrackInAppStateSettings(context);
        if (appState != null) {
            GeoSparkPref.removeItem(context, "APPSTATE");
            Set<String> newSet = new HashSet<>();
            newSet.addAll(appState);
            newSet.remove(value);
            GeoSparkPref.setTrackInAppStateSettings(context, newSet);
            if (newSet.size() == 0) {
                GeoSpark.setTrackingInAppState(context, new GeoSpark.Type[]{GeoSpark.Type.ALWAYS_ON});
            }
        }
    }

    public static void removeMotionStateItem(Context context, String value) {
        Set<String> appState = GeoSparkPref.getTrackInAppStateSettings(context);
        if (appState != null) {
            GeoSparkPref.removeItem(context, "MOTIONSTATE");
            Set<String> newSet = new HashSet<>();
            newSet.addAll(appState);
            newSet.remove(value);
            GeoSparkPref.setTrackInAppStateSettings(context, newSet);
            if (newSet.size() == 0) {
                GeoSpark.setTrackingInMotion(context, new GeoSpark.Type[]{GeoSpark.Type.ALL});
            }
        }
    }

    private Set<String> convertToStringSet(List<GeoSpark.Type> typeList) {
        LinkedList<String> list = new LinkedList<>();
        for (GeoSpark.Type cons : typeList) {
            list.add(cons.name());
        }
        return new HashSet<>(list);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

}
