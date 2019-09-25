package com.mecodroid.blood_bank.helper.server;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.view.activity.SplashSliderActivity;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationManager notificationManager;
    private static final String ADMIN_CHANNEL_ID = "admin_channel";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {

            //You should use an actual ID instead
            int notificationId = new Random().nextInt(60000);


            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels();
            }

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            Map<String, String> data = remoteMessage.getData();
//            if (title != null) {
//                title =
//            }

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                            .setSmallIcon(R.drawable.logo256)
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .setSummaryText(message))
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri);

            Intent resultIntent = new Intent(this, SplashSliderActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);

            notificationManager.notify(notificationId, notificationBuilder.build());

        } catch (Exception e) {

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        CharSequence adminChannelName = "";
        String adminChannelDescription = "";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}