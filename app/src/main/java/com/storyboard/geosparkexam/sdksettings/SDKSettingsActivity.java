package com.storyboard.geosparkexam.sdksettings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.geospark.lib.GeoSpark;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.presistence.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SDKSettingsActivity extends FragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private CheckBox foreground, background, terminated, always_on;
    private CheckBox stop, walk, drive, bicycle, all;
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
                    removeAppStateItem(SDKSettingsActivity.this, GeoSpark.Type.FOREGROUND.toString());
                }
                break;

            case R.id.ckb_background:
                if (isChecked) {
                    appStateList.add(GeoSpark.Type.BACKGROUND);
                    appState();
                } else {
                    appStateList.remove(GeoSpark.Type.BACKGROUND);
                    removeAppStateItem(SDKSettingsActivity.this, GeoSpark.Type.BACKGROUND.toString());
                }
                break;

            case R.id.ckb_terminated:
                if (isChecked) {
                    appStateList.add(GeoSpark.Type.TERMINATED);
                    appState();
                } else {
                    appStateList.remove(GeoSpark.Type.TERMINATED);
                    removeAppStateItem(SDKSettingsActivity.this, GeoSpark.Type.TERMINATED.toString());
                }
                break;

            case R.id.ckb_always_on:
                if (isChecked) {
                    appStateList.add(GeoSpark.Type.ALWAYS_ON);
                    appState();
                } else {
                    appStateList.remove(GeoSpark.Type.ALWAYS_ON);
                    removeAppStateItem(SDKSettingsActivity.this, GeoSpark.Type.ALWAYS_ON.toString());
                }
                break;

            case R.id.ckb_stop:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.STOP);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.STOP);
                    removeMotionStateItem(SDKSettingsActivity.this, GeoSpark.Type.STOP.toString());
                }
                break;

            case R.id.ckb_walk:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.WALK);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.WALK);
                    removeMotionStateItem(SDKSettingsActivity.this, GeoSpark.Type.WALK.toString());
                }
                break;

            case R.id.ckb_drive:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.DRIVE);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.DRIVE);
                    removeMotionStateItem(SDKSettingsActivity.this, GeoSpark.Type.DRIVE.toString());
                }
                break;

            case R.id.ckb_bicycle:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.BICYCLE);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.BICYCLE);
                    removeMotionStateItem(SDKSettingsActivity.this, GeoSpark.Type.BICYCLE.toString());
                }
                break;

            case R.id.ckb_all:
                if (isChecked) {
                    motionList.add(GeoSpark.Type.ALL);
                    motionState();
                } else {
                    motionList.remove(GeoSpark.Type.ALL);
                    removeMotionStateItem(SDKSettingsActivity.this, GeoSpark.Type.ALL.toString());
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdk_settings_activity);
        //AppState
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
        all.setOnCheckedChangeListener(this);

        showAppStateSettings();
        showMotionStateSettings();
    }

    private void showAppStateSettings() {
        Set<String> appState = SharedPreferenceHelper.getTrackInAppStateSettings(this);
        if (appState != null && appState.size() > 0) {
            for (String s : appState) {
                if (s.equals(GeoSpark.Type.FOREGROUND.toString())) {
                    foreground.setChecked(true);
                } else if (s.equals(GeoSpark.Type.BACKGROUND.toString())) {
                    background.setChecked(true);
                } else if (s.equals(GeoSpark.Type.TERMINATED.toString())) {
                    terminated.setChecked(true);
                } else if (s.equals(GeoSpark.Type.ALWAYS_ON.toString())) {
                    always_on.setChecked(true);
                }
            }
        }
    }

    private void showMotionStateSettings() {
        Set<String> motionState = SharedPreferenceHelper.getTrackingMotion(this);
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
                } else if (s.equals(GeoSpark.Type.ALL.toString())) {
                    all.setChecked(true);
                }
            }
        }
    }

    private void appState() {
        SharedPreferenceHelper.setTrackInAppStateSettings(this, convertToStringSet(appStateList));
        GeoSpark.setTrackingInAppState(this, appStateList.toArray(new GeoSpark.Type[appStateList.size()]));
    }

    private void motionState() {
        SharedPreferenceHelper.setTrackingMotion(this, convertToStringSet(motionList));
        GeoSpark.setTrackingInMotion(this, motionList.toArray(new GeoSpark.Type[motionList.size()]));
    }

    public static void removeAppStateItem(Context context, String value) {
        Set<String> appState = SharedPreferenceHelper.getTrackInAppStateSettings(context);
        if (appState != null) {
            SharedPreferenceHelper.removeItem(context, "APPSTATE");
            Set<String> newSet = new HashSet<>();
            newSet.addAll(appState);
            newSet.remove(value);
            SharedPreferenceHelper.setTrackInAppStateSettings(context, newSet);
            if (newSet.size() == 0) {
                GeoSpark.setTrackingInAppState(context, new GeoSpark.Type[]{GeoSpark.Type.ALWAYS_ON});
            }
        }
    }

    public static void removeMotionStateItem(Context context, String value) {
        Set<String> appState = SharedPreferenceHelper.getTrackInAppStateSettings(context);
        if (appState != null) {
            SharedPreferenceHelper.removeItem(context, "MOTIONSTATE");
            Set<String> newSet = new HashSet<>();
            newSet.addAll(appState);
            newSet.remove(value);
            SharedPreferenceHelper.setTrackInAppStateSettings(context, newSet);
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
