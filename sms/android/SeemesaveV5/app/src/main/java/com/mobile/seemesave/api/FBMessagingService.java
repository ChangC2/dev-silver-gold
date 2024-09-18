package com.mobile.seemesave.api;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

import com.mobile.seemesave.R;
import com.mobile.seemesave.utils.StringUtils;
import com.mobile.seemesave.utils.UriUtils;
import com.mobile.seemesave.view.auth.SplashActivity;

public class FBMessagingService extends FirebaseMessagingService {

    private int notification_id = 10;
    public FBMessagingService() {
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject notificationObj = new JSONObject(params);
                if (params.size() > 2){
                    String appName = notificationObj.getString("app_name");
                    String appDescription = notificationObj.getString("app_description");
                    String appLink = notificationObj.getString("app_link");
                    String appImage = notificationObj.getString("app_image");
                    showMessageApp(appName, appDescription, appLink, appImage);
                }else {
                    String title = notificationObj.getString("title");
                    String body = notificationObj.getString("body");
                    showMessageInterest(title, body);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void showMessageApp(String appName, String appDescription, String appLink, String appImage) {
        try {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder;
            NotificationChannel mChannel;
            String CHANNEL_ID = StringUtils.FIRE_CHANNEL_ID;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = StringUtils.FIRE_CHANNEL_NAME;
                String Description = StringUtils.FIRE_CHANNEL_DESCRIPTION;
                int importance = NotificationManager.IMPORTANCE_HIGH;
                mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);

                mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle(appName)
                        .setContentText(appDescription)
                        .setSmallIcon(R.drawable.ic_splash_logo)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);
            }else {
                mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle(appName)
                        .setContentText(appDescription)
                        .setSmallIcon(R.drawable.ic_splash_logo)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);
            }

            mBuilder.setSmallIcon(R.drawable.ic_splash_logo);
            try {
                if (TextUtils.isEmpty(appImage)){
                    mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_splash_logo));
                }else {
                    mBuilder.setLargeIcon(UriUtils.getBitmapFromUrl(appImage));
                }
            }catch (Exception e){
                e.printStackTrace();
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_splash_logo));
            }

            if (!TextUtils.isEmpty(appLink)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appLink));
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), (int) (Math.random() * 100), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);

                if (notification_id > 1073741824) {
                    notification_id = 10;
                }
                notificationManager.notify(notification_id, mBuilder.build());
                notification_id++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showMessageInterest(String title, String body) {
        try {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder;
            NotificationChannel mChannel;
            String CHANNEL_ID = StringUtils.FIRE_CHANNEL_ID;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = StringUtils.FIRE_CHANNEL_NAME;
                String Description = StringUtils.FIRE_CHANNEL_DESCRIPTION;
                int importance = NotificationManager.IMPORTANCE_HIGH;
                mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);

                mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.ic_splash_logo)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);
            }else {
                mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.ic_splash_logo)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);
            }

            mBuilder.setSmallIcon(R.drawable.ic_splash_logo);
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), (int) (Math.random() * 100), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);

            if (notification_id > 1073741824) {
                notification_id = 10;
            }
            notificationManager.notify(notification_id, mBuilder.build());
            notification_id++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}