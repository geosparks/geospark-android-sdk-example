package com.geospark.example.firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.geospark.example.MainActivity;
import com.geospark.example.R;
import com.geospark.lib.GeoSpark;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    public static final String CHANNEL_ID = "geospark_channel";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            Log.e("NOTIFY", remoteMessage.getData().toString());
            sendNotification(getApplicationContext(), remoteMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(Context context, RemoteMessage remoteMessage) {
        try {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.app_name);
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationManager.createNotificationChannel(mChannel);
            }
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.putExtra(GeoSpark.EXTRA, GeoSpark.notificationReceiveHandler(remoteMessage.getData()));
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(notificationIntent);
            PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.ic_geospark)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_geospark))
                    .setContentTitle(remoteMessage.getData().get("type"))
                    .setContentText(remoteMessage.getData().get("cid"))
                    .setContentIntent(notificationPendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID);
            }
            builder.setAutoCancel(true);
            mNotificationManager.notify(111, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
