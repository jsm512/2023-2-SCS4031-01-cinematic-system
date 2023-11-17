package com.example.fiebasephoneauth;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

//    public void onMessageReceived(RemoteMessage remoteMessage){
//        super.onMessageReceived(remoteMessage);
//        String title = remoteMessage.getNotification().getTitle();
//        String body = remoteMessage.getNotification().getBody();
//        final String CHANNEL_ID = "HEADS_UP_NOTIFICATIONS";
//        NotificationChannel channel = new NotificationChannel(
//                CHANNEL_ID,
//                "MyNotification",
//                NotificationManager.IMPORTANCE_HIGH);
//        getSystemService(NotificationManager.class).createNotificationChannel(channel);
//        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setSmallIcon(R.drawable.baseline_phone_android_24)
//                .setAutoCancel(true);
//        NotificationManagerCompat.from(this).notify(1,notification.build());
//    }
}



