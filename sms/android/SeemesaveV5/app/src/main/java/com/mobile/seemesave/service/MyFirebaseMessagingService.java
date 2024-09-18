package com.mobile.seemesave.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobile.seemesave.R;
import com.mobile.seemesave.application.App;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.checkout.CheckOutDeliverActivity;
import com.mobile.seemesave.view.menu.NotificationActivity;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String NotificationChannel = "NotificationChannel";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("SeeMeSave", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token);
    }

//    @SuppressLint("CommitPrefEdits")
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        try {
//            if (remoteMessage.getData().size() > 0) {
//                Map<String, String> params = remoteMessage.getData();
//                JSONObject object = new JSONObject(params);
//                String notificationType = object.optString("relType");
//                String title = object.optString("title");
//                String content = object.optString("body");
//                if (!TextUtils.isEmpty(notificationType) && notificationType.equalsIgnoreCase("DeliverCartStatusChange")) {
//                    App.getInstance().setDelverRel(object.optString("relId"), object.optString("body"));
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("show_notification"));
//                }
//                showNotification(title, content, object.optString("relId"), notificationType);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("TAG", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            try {
                if (remoteMessage.getData().size() > 0) {
                    Map<String, String> params = remoteMessage.getData();
                    JSONObject object = new JSONObject(params);
                    String notificationType = object.optString("relType");
                    String title = object.optString("title");
                    String content = object.optString("body");
                    if (!TextUtils.isEmpty(notificationType) && notificationType.equalsIgnoreCase("DeliverCartStatusChange")) {
                        App.getInstance().setDelverRel(object.optString("relId"), object.optString("body"));
                        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("show_notification"));
                    }
                    showNotification(title, content, object.optString("relId"), notificationType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), "", "");
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @SuppressLint("CommitPrefEdits")
    private void showNotification(String title, String content, String relId, String notificationType) {
        Intent intent = null;
        if (notificationType.equalsIgnoreCase("DeliverCartStatusChange")) {
            intent = new Intent(getApplicationContext(), CheckOutDeliverActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), NotificationActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        int requestId = 10;
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestId, intent, PendingIntent.FLAG_IMMUTABLE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), NotificationChannel);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_me);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
//        mBuilder.setSound(defaultSoundUri);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new NotificationChannel(NotificationChannel, NotificationChannel, NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }
        mNotificationManager.notify(requestId, mBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isAppInForeground(Context context) {
        List<ActivityManager.RunningTaskInfo> task =
                ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                        .getRunningTasks(1);
        if (task.isEmpty()) {
            return false;
        }
        return task
                .get(0)
                .topActivity
                .getPackageName()
                .equalsIgnoreCase(context.getPackageName());
    }
}