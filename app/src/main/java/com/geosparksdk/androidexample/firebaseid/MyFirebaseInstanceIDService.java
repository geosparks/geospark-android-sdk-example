package com.geosparksdk.androidexample.firebaseid;

import com.geosparksdk.androidexample.presistence.SharedPreferenceHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferenceHelper.setStringText(getApplicationContext(), "DEVICE_TOKEN", refreshedToken);
    }
}