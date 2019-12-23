package com.example.booktobook.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.QuickContactBadge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booktobook.Adapter.ChatListAdapter;
import com.example.booktobook.Model.Chat;
import com.example.booktobook.Interface.OnItemClickListener;
import com.example.booktobook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Chat> chats = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageButton chatBackButton;
    DocumentReference documentReference;
    String id;
    Chat chat;
    Context mcontext=this;
    int cnt;
    ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.recycler_view_chat);
        chatBackButton = findViewById(R.id.activity_chat_back_button);

        chatBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        id = sharedPreferences.getString("ID", "id");
        chatListAdapter= new ChatListAdapter(chats);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //for test
      //  id = "1234";


        db.collection("room")
                .whereEqualTo("haver",id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        if (snapshot.exists()) {

                            chat= new Chat();
                            //방 Id
                            chat.id = snapshot.getId();
//
//                            db.collection("chat")
//                                    .whereEqualTo("room_id",id)
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            cnt=0;
//                                            final String message[]=new String[task.getResult().size()];
//                                            final String time[]=new String[task.getResult().size()];
//                                            if (task.isSuccessful()){
//                                                for (QueryDocumentSnapshot snapshot:task.getResult())
//                                                {
//                                                    message[cnt]=snapshot.getString("message");
//                                                    time[cnt]=snapshot.getString("timestamp");
//                                                    cnt++;
//                                                }
//                                                chat.latestmessage=message[0];
//                                                chat.latesttime=time[0];
//
//                                                Log.d("test",chat.latestmessage);
//                                                Log.d("test",chat.latesttime);
//
//
//                                                chatListAdapter.notifyDataSetChanged();
//                                            }
//                                        }
//                                    });
                            //방 이름
                            chat.haver = snapshot.getString("haver");
                            chat.borrower = snapshot.getString("borrower");

                            chat.roomName = chat.borrower;
                            //chat.name 에다 자기 id 넣어줌.
                            chat.name = id;
                            chats.add(chat);


                            Log.d("kwk",chat.haver+" / "+chat.roomName);
                        }

                    }
                    chatListAdapter.notifyDataSetChanged();
                } else {
                    Log.d("asdd","test fail");
                }
            }
        });


        db.collection("room")
                .whereEqualTo("borrower",id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                if (snapshot.exists()) {

                                         chat= new Chat();
                                        //방 Id
                                        chat.id = snapshot.getId();
//                                    db.collection("chat")
//                                            .whereEqualTo("room_id",id)
//                                            .get()
//                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                    cnt=0;
//                                                    final String message[]=new String[task.getResult().size()];
//                                                    final String time[]=new String[task.getResult().size()];
//                                                    if (task.isSuccessful()){
//                                                        for (QueryDocumentSnapshot snapshot:task.getResult())
//                                                        {
//                                                            message[cnt]=snapshot.getString("message");
//                                                            time[cnt]=snapshot.getString("timestamp");
//                                                            cnt++;
//                                                        }
//                                                        chat.latestmessage=message[0];
//                                                        chat.latesttime=time[0];
//
//
//                                                        Log.d("test",chat.latestmessage);
//                                                        Log.d("test",chat.latesttime);
//
//
//                                                        chatListAdapter.notifyDataSetChanged();
//                                                    }
//                                                }
//                                            });
                                        //방 이름
                                        chat.haver = snapshot.getString("haver");
                                        chat.borrower = snapshot.getString("borrower");
                                        chat.roomName = chat.haver;
                                        //chat.name 에다 자기 id 넣어줌.
                                        chat.name = id;

                                        chats.add(chat);

                                        Log.d("kwk",chat.haver+" / "+chat.roomName+"test2");
                                        Log.d("test",chat.latestmessage);


                                }
                            }
                            chatListAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("asdd","test fail");
                        }
                    }
                });

        Log.d("qwer", String.valueOf(chats.size()));

//
//
//
//        final ChatListAdapter adapter = new ChatListAdapter(chats);
//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent i = new Intent(ChatActivity.this, MessageActivity.class);
//                i.putExtra("room_data", adapter.getItem(position));
//                startActivity(i);
//            }
//        });
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//





//        documentReference= (DocumentReference) db.collection("room")
//                .orderBy("name").startAt(id)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//                        List<Chat> rooms= new ArrayList<>();
//                        for (QueryDocumentSnapshot doc:queryDocumentSnapshots){
//                            Chat chat= new Chat();
//                            //방 Id
//                            chat.id=doc.getId();
//                            //방 이름
//                            chat.haver=doc.getString("haver");
//                            chat.borrower=doc.getString("borrower");
//                            //책을 빌린 사람의 경우
//                            if (chat.borrower.equals(id))
//                            {
//                                chat.roomName=chat.haver;
//                            }
//                            //책을 빌려준 사람의 경우
//                            else
//                            {
//                                chat.roomName=chat.borrower;
//                            }
//                            //chat.name 에다 자기 id 넣어줌.
//                            chat.name=id;
//                            rooms.add(chat);
//                        }
////                        chats= (ArrayList<Chat>) rooms;
//                        final ChatListAdapter adapter = new ChatListAdapter(chats);
                        chatListAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent i = new Intent(ChatActivity.this, MessageActivity.class);
                                i.putExtra("room_data",chatListAdapter.getItem(position));
                                startActivity(i);
                            }
                        });
//                        recyclerView.setAdapter(adapter);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//                    }
//                });
//

        }

    private void getProfile(final String id) {

        new Thread(new Runnable() {
            @Override
            public void run() {

            }

        }).start();



    }

//    private  String reMessage() {
//        return chat.latestmessage;
//    }
//    private String reTime(){
//        return chat.latesttime;
//    }
}
