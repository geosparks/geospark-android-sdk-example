package com.storyboard.geosparkexam.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.storyboard.geosparkexam.R;

import java.util.Random;

public class NotifyUtil {
    //Notification ID
    private static final String ANDROID_CHANNEL_ID = "com.geospark.ANDROID";
    private static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";

    private static int getID() {
        return new Random().nextInt(100000000);
    }

    public static void showNotification(Context context, String title, String content) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(ANDROID_CHANNEL_ID, ANDROID_CHANNEL_NAME, importance);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context, notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        builder = builder
                .setSmallIcon(R.drawable.ic_geospark)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
        notificationManager.notify(getID(), builder.build());
    }
}
