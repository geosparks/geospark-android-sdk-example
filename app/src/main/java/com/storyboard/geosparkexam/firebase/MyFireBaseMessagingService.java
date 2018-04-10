package com.storyboard.geosparkexam.firebase;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.storyboard.geosparkexam.util.NotifyUtil;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String questionTitle = remoteMessage.getData().get("title");
        String userDisplayName = remoteMessage.getData().get("message");
        NotifyUtil.showNotification(getApplicationContext(), questionTitle, userDisplayName);
    }
}