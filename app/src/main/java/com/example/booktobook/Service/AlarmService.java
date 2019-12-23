package com.example.booktobook.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class AlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
//        final String SERVER_KEY = "AAAAvwFFAB0:APA91bH27fELBmCwMY1ND4vQVUeaKmujW-k0N72NDzhJDaoV4IQ9z-KHcfS1UePQ_bGUKK2vWbJRmFLD_6txrpS8BJj5tpU1NKashowU-6jat4RW5aaPeQVHn9m6y7ZHlPqCJi4y1kB9";
//        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
//        String receiver=intent.getStringExtra("receiver");
//        final String[] token = {""};
//        firebaseFirestore.collection("Users")
//                .document(receiver)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                        token[0] = documentSnapshot.getString("token");
//
//                    }
//                });
//
//        try {
//            Log.d("##","test1");
//            JSONObject root = new JSONObject();
//            JSONObject notification = new JSONObject();
//            String data = "책을 받았습니까?";
//            notification.put("body", data);
//            notification.put("title","BookToBook");
//            root.put("notification",notification);
//            root.put("to",token[0]);
//            root.put("click_action","CHAT_ACTIVITY");
//            URL url= new URL(FCM_MESSAGE_URL);
//            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            connection.addRequestProperty("Authorization", "key=" + SERVER_KEY);
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setRequestProperty("Content-type", "application/json");
//            OutputStream os=connection.getOutputStream();
//            os.write(root.toString().getBytes("utf-8"));
//            os.flush();
//
//            Log.d("service","test sucess");
//
//            connection.getResponseCode();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }



        return super.onStartCommand(intent, flags, startId);


    }
}
