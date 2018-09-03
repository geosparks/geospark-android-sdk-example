package com.storyboard.geosparkexam.sdksettings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.geospark.lib.GeoSpark;
import com.storyboard.geosparkexam.R;
import com.storyboard.geosparkexam.presistence.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SDKSettingsActivity extends FragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private CheckBox foreground, background, terminated, always_on;
    private CheckBox stop, walk, drive, bicycle, all;
    private RadioGroup rg_locationMode, rg_locationFrequency, rg_locationAccuracy, rg_distance;
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
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (radioGroup.getId()) {
            case R.id.radioGroup_mode:
                RadioButton rb_mode = radioGroup.findViewById(checkedId);
                int mode_index = radioGroup.indexOfChild(rb_mode);
                locationMode(mode_index);
                break;

            case R.id.radioGroup_freq:
                RadioButton rb_freq = radioGroup.findViewById(checkedId);
                int mode_freq = radioGroup.indexOfChild(rb_freq);
                locationFreq(mode_freq);
                break;

            case R.id.radioGroup_acc:
                RadioButton rb_acc = radioGroup.findViewById(checkedId);
                int mode_acc = radioGroup.indexOfChild(rb_acc);
                locationAccuracy(mode_acc);
                break;

            case R.id.radioGroup_dis:
                RadioButton rb_dis = radioGroup.findViewById(checkedId);
                int mode_dis = radioGroup.indexOfChild(rb_dis);
                distanceFilter(mode_dis);
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
        //Location Settings
        rg_locationMode = findViewById(R.id.radioGroup_mode);
        rg_locationFrequency = findViewById(R.id.radioGroup_freq);
        rg_locationAccuracy = findViewById(R.id.radioGroup_acc);
        rg_distance = findViewById(R.id.radioGroup_dis);

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
        rg_locationMode.setOnCheckedChangeListener(this);
        rg_locationFrequency.setOnCheckedChangeListener(this);
        rg_locationAccuracy.setOnCheckedChangeListener(this);
        rg_distance.setOnCheckedChangeListener(this);

        showAppStateSettings();
        showMotionStateSettings();
        showLocationMode();
        showLocationFrequency();
        showLocationAccuracy();
        showDistanceFilter();
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

    private void showLocationMode() {
        String index = SharedPreferenceHelper.getLocMode(this);
        if (index != null) {
            RadioButton rb = (RadioButton) rg_locationMode.getChildAt(Integer.valueOf(index));
            rb.setChecked(true);
        }
    }

    private void showLocationFrequency() {
        String index = SharedPreferenceHelper.getLocFreq(this);
        if (index != null) {
            RadioButton rb = (RadioButton) rg_locationFrequency.getChildAt(Integer.valueOf(index));
            rb.setChecked(true);
        }
    }

    private void showLocationAccuracy() {
        String index = SharedPreferenceHelper.getLocAcc(this);
        if (index != null) {
            RadioButton rb = (RadioButton) rg_locationAccuracy.getChildAt(Integer.valueOf(index));
            rb.setChecked(true);
        }
    }

    private void showDistanceFilter() {
        String index = SharedPreferenceHelper.getDistance(this);
        if (index != null) {
            RadioButton rb = (RadioButton) rg_distance.getChildAt(Integer.valueOf(index));
            rb.setChecked(true);
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

    private void locationMode(int index) {
        switch (index) {
            case R.id.rb_high:
                GeoSpark.setLocationMode(SDKSettingsActivity.this, GeoSpark.Type.HIGH_ACCURACY);
                break;
            case R.id.rb_medium:
                GeoSpark.setLocationMode(SDKSettingsActivity.this, GeoSpark.Type.BALANCED_POWER_ACCURACY);
                break;
            case R.id.rb_low:
                GeoSpark.setLocationMode(SDKSettingsActivity.this, GeoSpark.Type.LOW_POWER);
                break;
        }
        SharedPreferenceHelper.setLocMode(SDKSettingsActivity.this, String.valueOf(index));
    }

    private void locationFreq(int index) {
        switch (index) {
            case R.id.rb_high_f:
                GeoSpark.setLocationFrequency(SDKSettingsActivity.this, GeoSpark.Type.HIGH);
                break;
            case R.id.rb_medium_f:
                GeoSpark.setLocationFrequency(SDKSettingsActivity.this, GeoSpark.Type.MEDIUM);
                break;
            case R.id.rb_low_f:
                GeoSpark.setLocationFrequency(SDKSettingsActivity.this, GeoSpark.Type.LOW);
                break;
            case R.id.rb_optimized_f:
                GeoSpark.setLocationFrequency(SDKSettingsActivity.this, GeoSpark.Type.OPTIMISED);
                break;
        }
        SharedPreferenceHelper.setLocFreq(SDKSettingsActivity.this, String.valueOf(index));
    }

    private void locationAccuracy(int index) {
        switch (index) {
            case R.id.rb_high_a:
                GeoSpark.setLocationAccuracy(SDKSettingsActivity.this, GeoSpark.Type.HIGH);
                break;
            case R.id.rb_medium_a:
                GeoSpark.setLocationAccuracy(SDKSettingsActivity.this, GeoSpark.Type.MEDIUM);
                break;
            case R.id.rb_low_a:
                GeoSpark.setLocationAccuracy(SDKSettingsActivity.this, GeoSpark.Type.LOW);
                break;
            case R.id.rb_optimized_a:
                GeoSpark.setLocationAccuracy(SDKSettingsActivity.this, GeoSpark.Type.OPTIMISED);
                break;
        }
        SharedPreferenceHelper.setLocAcc(SDKSettingsActivity.this, String.valueOf(index));
    }

    private void distanceFilter(int index) {
        switch (index) {
            case R.id.rb_high_d:
                GeoSpark.setDistanceFilter(SDKSettingsActivity.this, GeoSpark.Type.HIGH);
                break;
            case R.id.rb_medium_d:
                GeoSpark.setDistanceFilter(SDKSettingsActivity.this, GeoSpark.Type.MEDIUM);
                break;
            case R.id.rb_low_d:
                GeoSpark.setDistanceFilter(SDKSettingsActivity.this, GeoSpark.Type.LOW);
                break;
            case R.id.rb_optimized_d:
                GeoSpark.setDistanceFilter(SDKSettingsActivity.this, GeoSpark.Type.OPTIMISED);
                break;
        }
        SharedPreferenceHelper.setDistance(SDKSettingsActivity.this, String.valueOf(index));
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
