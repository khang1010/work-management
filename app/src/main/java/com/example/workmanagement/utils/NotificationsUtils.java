package com.example.workmanagement.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.activities.BlankActivity;
import com.example.workmanagement.activities.HomeActivity;
import com.example.workmanagement.utils.dto.MessageDTO;
import com.example.workmanagement.utils.dto.NotificationDTO;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class NotificationsUtils {

    private static NotificationsUtils instance;

    public static NotificationsUtils getInstance() {
        if (instance == null)
            instance = new NotificationsUtils();
        return instance;
    }

    public void createNotification(Context context, MessageDTO message) throws ExecutionException, InterruptedException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MY_NOTIFICATION",
                    "My Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, BlankActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_NOTIFICATION")
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(Glide.with(context).asBitmap().load(message.getPhotoUrl().equals("null") ? BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default) : message.getPhotoUrl()).submit().get())
                .setStyle(new NotificationCompat.BigPictureStyle())
                .setContentTitle(message.getBoardName())
                .setContentText(message.getDisplayName() + ": " + message.getMessage())
                .setFullScreenIntent(null, true)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker("Notification");
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(new Random().nextInt(), builder.build());
    }

    public void createNotification(Context context, NotificationDTO notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MY_NOTIFICATION",
                    "My Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, BlankActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_NOTIFICATION")
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_logo3x))
                .setStyle(new NotificationCompat.BigPictureStyle())
                .setContentTitle("Notification")
                .setContentText(notification.getMessage())
                .setFullScreenIntent(null, true)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker("Notification");
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(new Random().nextInt(), builder.build());
    }

    private NotificationsUtils() {}

}
