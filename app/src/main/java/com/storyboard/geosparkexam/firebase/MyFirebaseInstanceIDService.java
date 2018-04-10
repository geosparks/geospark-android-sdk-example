package com.storyboard.geosparkexam.firebase;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.storyboard.geosparkexam.presistence.SharedPreferenceHelper;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferenceHelper.saveDeviceToken(getApplicationContext(), refreshedToken);
    }
}