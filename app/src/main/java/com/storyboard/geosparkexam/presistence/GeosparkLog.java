package com.storyboard.geosparkexam.presistence;


import android.content.Context;

public class GeosparkLog {

    private String mId;
    private String mName;
    private String mComments;
    private String mLat;
    private String mLng;
    private String mSpeed;
    private String mDateTime;
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
}
