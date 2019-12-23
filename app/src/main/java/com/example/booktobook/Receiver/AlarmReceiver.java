package com.example.booktobook.Receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.booktobook.Activity.MainActivity;
import com.example.booktobook.R;
import com.example.booktobook.Service.AlarmService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        String text = intent.getStringExtra("text");
        int id = intent.getIntExtra("id", 0);
        String room_id = intent.getStringExtra("room_id");

//        String receiver=intent.getStringExtra("receiver");

        //Log.d("test",room_id+"\n"+receiver+"\n"+text);

        String receiver="";

        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.library); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            String channelName = "channelName";
            String description = "alarm";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌
            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                // 노티피케이션 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        } else
            builder.setSmallIcon(R.mipmap.library); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        long[] pattern = {1000, 50, 1000, 50};
        vibrator.vibrate(pattern, -1);

        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("BookToBook")
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setVibrate(pattern);

        if (notificationManager != null) {
            // 노티피케이션 동작시킴
            SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
            String haver = sharedPreferences.getString("haver", "");
            Log.d("haver",haver);
            Map<String,Object> map= new HashMap<>();
            map.put("flag",true);
            FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users")
                    .document(haver).update(map);


            notificationManager.notify(id, builder.build());


            Intent serviceIntent = new Intent(context, AlarmService.class);
            Log.d("service", receiver + "\n" + room_id);
            serviceIntent.putExtra("receiver", receiver);
            serviceIntent.putExtra("room_id", room_id);

            context.startService(serviceIntent);


        }
    }
}
