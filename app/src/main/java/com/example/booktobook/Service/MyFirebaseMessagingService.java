package com.example.booktobook.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.booktobook.Activity.ChatActivity;
import com.example.booktobook.Activity.MessageActivity;
import com.example.booktobook.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = FirebaseMessagingService.class.getSimpleName();
    private static final String REPLY_KEY = "reply";
    private static final String REPLY_LABEL = "Input reply";
    String token;


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: token=" + s);
        //TODO:서버로 토큰을 보내서 저장한다
        token = s;


        sendRegistrationToServer(s);


    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getData().get("click_action");
            Log.d("####", "onMessageReceived: "+click_action);
            sendNotification(remoteMessage, title, body, click_action);
        }


    }


    private void sendNotification(RemoteMessage remoteMessage, String title, String body, String click_action) {


        Intent intent = null;
        if (click_action.equals("CHAT_ACTIVITY")) {
            intent = new Intent(this, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Log.d("chat", "chat");
        } else if (click_action.equals("MESSAGE_ACTIVITY")) {
            intent = new Intent(this, MessageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        RemoteInput remoteInput = new RemoteInput.Builder(REPLY_KEY)
                .setLabel(REPLY_LABEL)
                .build();

        //Intent intent= new Intent(getApplicationContext(),MainActivity.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = "Channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher_round, REPLY_LABEL, pendingIntent)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .addAction(action)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Channel Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

            builder.setChannelId(channelId);
        }
        notificationManager.notify(0, builder.build());
    }

    private void sendRegistrationToServer(String token) {


//        Map<String,String> map= new HashMap<>();
//        map.put("token",token);

    }


}

