package com.geospark.example.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.geospark.example.R;
import com.geospark.example.ui.MainActivity;
import com.geospark.lib.GeoSpark;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    public static final String CHANNEL_ID = "geospark_channel";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        if (token != null) {
            GeoSpark.setDeviceToken(getApplicationContext(), token);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            sendNotification(getApplicationContext(), remoteMessage);
        } catch (Exception e) {
        }
    }

    private void sendNotification(Context context, RemoteMessage remoteMessage) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.app_name);
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(GeoSpark.EXTRA, GeoSpark.notificationReceiveHandler(remoteMessage.getData()));
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(intent);
            PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_geospark)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_geospark))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(remoteMessage.getData().get("message")))
                    .setContentIntent(notificationPendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID);
            }
            builder.setAutoCancel(true);
            notificationManager.notify(getId(), builder.build());
        } catch (Exception e) {
        }
    }


    private int getId() {
        return new Random().nextInt(100000);
    }
}

