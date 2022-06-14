package com.example.greenscene.Functionalities.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.greenscene.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class GreenSceneMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Map<String, String> data = message.getData();
        String title = data.get("title");
        String notifMessage = data.get("message");
        String deepLink = data.get("deepLink");

        sendNotification(this, title, notifMessage, deepLink);
    }

    public static void sendNotification(Context context, String title, String message, String deepLink){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("greensceneId","greensceneChannel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications from GreenScene App");
            notificationManager.createNotificationChannel(channel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

        Intent notifIntent = new Intent();
        notifIntent.setAction(Intent.ACTION_VIEW);
        notifIntent.setData(Uri.parse(deepLink));
        notifIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, notifIntent, PendingIntent.FLAG_ONE_SHOT);

        notificationBuilder.setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pi);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
