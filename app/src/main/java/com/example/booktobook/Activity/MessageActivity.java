package com.example.booktobook.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.RemoteInput;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booktobook.Adapter.MessageListAdapter;
import com.example.booktobook.Dialog.FormDialog;
import com.example.booktobook.Receiver.AlarmReceiver;
import com.example.booktobook.Model.Chat;
import com.example.booktobook.Interface.CustomDialogListener;
import com.example.booktobook.R;
import com.example.booktobook.Receiver.DeviceBootReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MessageActivity extends AppCompatActivity {

    Intent intent,intent2;
    ImageView back,plus;
    TextView name;
    Chat chat;
    EditText edit_chat;
    Button send;
    RecyclerView recyclerView;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    List<Chat> chats= new ArrayList<>();
    Context mcontext=this;
    SharedPreferences sharedPreferences;
    String sender;
    MessageListAdapter messageListAdapter;
    private SimpleDateFormat dateFormatHour= new SimpleDateFormat("aa hh:mm");
    FormDialog formDialog;
    int position;
    private SwitchDateTimeDialogFragment dateTimeFragment;
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    String a="";
    private Calendar calendar= Calendar.getInstance();
    private Calendar calendar1=Calendar.getInstance();
    ListenerRegistration listenerRegistration;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        reply(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        plus=findViewById(R.id.plusBtn);
        back=findViewById(R.id.backBtn);
        name=findViewById(R.id.chatName);
        recyclerView=findViewById(R.id.messages_view);
        edit_chat=findViewById(R.id.edittext_chatbox);
        send=findViewById(R.id.button_chatbox_send);
        intent=getIntent();
        intent2=this.getIntent();
        chat= (Chat) intent.getExtras().getSerializable("room_data");
        name.setText(chat.roomName);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dateFormatHour.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        bookAlarm();
        sharedPreferences=getSharedPreferences("pref",MODE_PRIVATE);
        sender=sharedPreferences.getString("ID","");
        position=sharedPreferences.getInt("size",0);
        recyclerView.scrollToPosition(position-1);
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);



        if (sender.equals(chat.haver))
        {
            plus.setVisibility(View.VISIBLE);

        }
        else{
            plus.setVisibility(View.GONE);
        }
        openChat();
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                formDialog= new FormDialog(MessageActivity.this,negativeListener);

                final String[] haver = {""};
                final String[] borrower = {""};
                db.collection("room")
                        .document(chat.id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful())
                        {
                            DocumentSnapshot snapshot=task.getResult();
                            haver[0] =snapshot.getString("haver");
                            borrower[0] =snapshot.getString("borrower");

                            SharedPreferences sharedPreferences=getSharedPreferences("pref",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("haver",haver[0]);
                            editor.apply();
                            editor.commit();



                            Log.d("iam",haver[0]);
                        }
                        formDialog.setText(haver[0],borrower[0]);
                    }
                });

                formDialog.show();

                formDialog.setDialogListner(new CustomDialogListener() {
                    @Override
                    public void onPostiveClicked(String sender, String receiver, final String time, final String location, String alarm) {

                        formDialog.dismiss();

                        final Map<String,Object> map = new HashMap<>();
                        map.put("sender",sender);
                        map.put("receiver",receiver);
                        map.put("time",time);
                        map.put("location",location);
                        map.put("room_id",chat.id);
                        map.put("flag",false);

                        db.collection("meet")
                                .add(map)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(),time+"에"+"\n"+location+"에서 만납니다.",Toast.LENGTH_SHORT).show();
                                            sendMessage(time+"에"+"\n"+location+"에서 만납니다.");
                                            SharedPreferences sharedPreferences=getSharedPreferences("pref",MODE_PRIVATE);
                                            SharedPreferences.Editor editor=sharedPreferences.edit();
                                            editor.putString("room_id",chat.id);
                                            editor.putString("receiver",chat.borrower);

                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"약속이 잡히지 않았습니다. -서버오류-",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        setAlarm();

                    }

                    @Override
                    public void onNegativeClicked() {

                        formDialog.dismiss();
                    }

                    @Override
                    public void onTimeClicked() {
                        setDateDialog();
                        showDateDialog();

                    }

                    @Override
                    public void onLocationClicked() {

                        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
                        builder.setAndroidApiKey("AIzaSyBmueYoyqNokkjvE2Ty926iWW0zOD-HeCA")
                                .setMapsApiKey("AIzaSyCgPNZtuNI7WmmVThQvkRbRi4N4p_wADE8");

                        try{
                            Intent placeIntent=builder.build(MessageActivity.this);
                            startActivityForResult(placeIntent,100);
                        }catch (Exception e)
                        {

                        }

                    }
                });


            }
        });

        messageListAdapter= new MessageListAdapter(this,chats,sender);
        recyclerView.setAdapter(messageListAdapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(edit_chat.getText().toString().equals("")))
                    sendMessage(edit_chat.getText().toString());


            }
        });


    }

    private void bookAlarm() {

//        Query query=db.collection("meet")
//                .whereEqualTo("room_id",chat.id)
//                .whereEqualTo("flag",false);

//        listenerRegistration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                Log.d("start","2");
//                setAlarm();
//                Log.d("start","1");
//                listenerRemove();
//            }
//        });


    }

//    private void listenerRemove() {
//        listenerRegistration.remove();
//    }

    private void setAlarm() {
            Intent mAlarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            mAlarmIntent.putExtra("text","책을 빌려주셨습니까?");
            mAlarmIntent.putExtra("id",111);

            PendingIntent mPendingIntent= PendingIntent.getBroadcast(
                    getApplicationContext(),0,mAlarmIntent,PendingIntent.FLAG_UPDATE_CURRENT
            );
            PendingIntent pendingIntent=PendingIntent.getBroadcast(
                    getApplicationContext(),1,mAlarmIntent,PendingIntent.FLAG_UPDATE_CURRENT
            );
            //mAlarmIntent.putExtra("text","약속 1시간 전입니다.");

        SharedPreferences preferences=getPreferences(MODE_PRIVATE);


            mAlarmIntent.putExtra("id",222);
            mAlarmIntent.putExtra("room_id",preferences.getString("room_id"," "));
            mAlarmIntent.putExtra("receiver",preferences.getString("receiver"," "));

            Log.d("service",preferences.getString("room_id"," ")+"\n"+preferences.getString("receiver"," "));



            PackageManager pm=getApplicationContext().getPackageManager();
            ComponentName receiver= new ComponentName(getApplicationContext(), DeviceBootReceiver.class);
            AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
            AlarmManager manager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);

            calendar1.add(Calendar.HOUR,-1);

        Log.d("else", String.valueOf(calendar1.getTimeInMillis()));
        Log.d("else", String.valueOf(calendar1.getTime()));

            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){

                    alarmManager.setExact(
                            manager.RTC_WAKEUP,
                            calendar1.getTimeInMillis(),
                            pendingIntent
                    );
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            mPendingIntent
                    );
                }else{
                    alarmManager.set(
                            manager.RTC_WAKEUP,
                            calendar1.getTimeInMillis(),
                            pendingIntent
                    );
                    alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            pendingIntent
                    );
                }
            }else{
                alarmManager.setExactAndAllowWhileIdle(
                        manager.RTC_WAKEUP,
                        calendar1.getTimeInMillis(),
                        mPendingIntent
                );

                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        mPendingIntent
                );
            }


            Log.d("cal", String.valueOf(calendar.getTime()));

            SharedPreferences sharedPreferences=getSharedPreferences("pref",MODE_PRIVATE);
            SharedPreferences.Editor editer=sharedPreferences.edit();
            editer.putLong("long",calendar.getTimeInMillis());
            editer.apply();
            editer.commit();

            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);


        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==100&&resultCode==RESULT_OK) {

            Place place=PingPlacePicker.getPlace(data);
            formDialog.setLocationText(place.getAddress());

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void showDateDialog() {
        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(2019, Calendar.NOVEMBER, 15, 15, 20).getTime());
        dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
    }

    private void setDateDialog() {

        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    "만날 시간",
                    "ok",
                    "cancel"
            );
        }
        dateTimeFragment.setTimeZone(TimeZone.getDefault());
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 hh시 mm분", Locale.getDefault());
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setHighlightAMPMSelection(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());

        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e("tag", e.getMessage());
        }

        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                calendar.setTime(date);
                calendar1.setTime(date);
                a=myDateFormat.format(date);

                formDialog.setTimeText(a);
                dateTimeFragment.dismiss();
                //Toast.makeText(getApplicationContext(),a,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                dateTimeFragment.dismiss();
            }
        });

    }

    private View.OnClickListener negativeListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            formDialog.dismiss();
        }
    };

    private void sendMessage(final String toString) {
        final HashMap<String,Object> map= new HashMap<>();
        map.put("room_id",chat.id);
        map.put("message",toString);
        map.put("sender",sender);
        map.put("receiver",chat.roomName);
        map.put("haver",chat.haver);
        map.put("sent",System.currentTimeMillis());
        chat.timestamp=dateFormatHour.format(new Date());
        map.put("timestamp",chat.timestamp);


        db.collection("chat")
                .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                sendFcm(toString);
            }
        });
        edit_chat.setText("");
        openChat();

    }
    private void reply(Intent intent){
        Bundle bundle = RemoteInput.getResultsFromIntent(intent);

        if(bundle != null){

            String messageText = bundle.getString("reply");
            sendMessage(messageText);
        }

    }

    private void sendFcm(final String toString) {
        final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        final String SERVER_KEY = "AAAAvwFFAB0:APA91bH27fELBmCwMY1ND4vQVUeaKmujW-k0N72NDzhJDaoV4IQ9z-KHcfS1UePQ_bGUKK2vWbJRmFLD_6txrpS8BJj5tpU1NKashowU-6jat4RW5aaPeQVHn9m6y7ZHlPqCJi4y1kB9";
        documentReference=db.collection("Users")
                .document(chat.roomName);

        final DocumentReference finalDocumentReference = documentReference;
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                final String token = (String) documentSnapshot.get("token");
                Log.d("yourToken",token);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject root = new JSONObject();
                            JSONObject notification = new JSONObject();
                            String data =sender+" : "+toString;
                            notification.put("body", data);
                            notification.put("title","BookToBook");
                            root.put("notification",notification);
                            root.put("to",token);
                            root.put("click_action","MESSAGE_ACTIVITY");

                            URL url= new URL(FCM_MESSAGE_URL);
                            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            connection.setDoInput(true);
                            connection.addRequestProperty("Authorization", "key="+SERVER_KEY);
                            connection.setRequestProperty("Accept", "application/json");
                            connection.setRequestProperty("Content-type", "application/json");
                            OutputStream os=connection.getOutputStream();
                            os.write(root.toString().getBytes("utf-8"));
                            os.flush();
                            connection.getResponseCode();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("yourToken", "fail");
            }
        });
    }

    private void openChat() {
        //처음에 db 메세지 가져오기
        db.collection("chat")
                .whereEqualTo("room_id",chat.id)
                .orderBy("sent", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e !=null){
                            return;
                        }
                        chats.clear();
                            for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                            {
                                Chat chat = new Chat();
                                chat.id=snapshot.getString("room_id");
                                chat.message=snapshot.getString("message");
                                chat.sender=snapshot.getString("sender");
                                chat.timestamp=snapshot.getString("timestamp");

                                chats.add(chat);
                            }
                            messageListAdapter.notifyDataSetChanged();
                            SharedPreferences sharedPreferences= getSharedPreferences("pref",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putInt("size",chats.size());
                            editor.commit();

                        }

                });

        }




            }




