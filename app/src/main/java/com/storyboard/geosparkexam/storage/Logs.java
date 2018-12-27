package com.storyboard.geosparkexam.storage;


import android.content.Context;

public class Logs {

    private String mId;
    private String mName;
    private String mComments;
    private String mLat;
    private String mLng;
    private String mSpeed;
    private String mDateTime;
    private String mFilterDate;
    private String mActivityType;
    private String mAccuracy;
    private static GeosparkDB mGeosparkDB;

    public static GeosparkDB getInstance(Context context) {
        if (mGeosparkDB == null) {
            mGeosparkDB = new GeosparkDB(context);
        }
        return mGeosparkDB;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmComments() {
        return mComments;
    }

    public void setmComments(String mComments) {
        this.mComments = mComments;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLng() {
        return mLng;
    }

    public void setmLng(String mLng) {
        this.mLng = mLng;
    }

    public String getmSpeed() {
        return mSpeed;
    }

    public void setmSpeed(String mSpeed) {
        this.mSpeed = mSpeed;
    }

    public String getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getmFilterDate() {
        return mFilterDate;
    }

    public void setmFilterDate(String mFilterDate) {
        this.mFilterDate = mFilterDate;
    }

    public String getmActivityType() {
        return mActivityType;
    }

    public void setmActivityType(String mActivityType) {
        this.mActivityType = mActivityType;
    }

    public String getmAccuracy() {
        return mAccuracy;
    }

    public void setmAccuracy(String mAccuracy) {
        this.mAccuracy = mAccuracy;
    }
}
