package com.example.booktobook.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booktobook.Adapter.AdapterAlert;
import com.example.booktobook.Model.Alert;
import com.example.booktobook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlertActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Alert> dataArrayList;
    public ImageButton alertactivity_backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        recyclerView = findViewById(R.id.recycler_view_alert);
        alertactivity_backbutton = findViewById(R.id.activity_alert_back_button);
        alertactivity_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        SharedPreferences pref = this.getSharedPreferences("pref", MODE_PRIVATE);
        String id = pref.getString("ID", "");

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //set adapter
        dataArrayList = new ArrayList<>();
        adapter = new AdapterAlert(dataArrayList,id);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();



        DocumentReference documentReference = db.collection("Users").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        if (documentSnapshot.getData().get("alert") != null) {
                            List list = (List) documentSnapshot.getData().get("alert");
                            for (int i = 0; i < list.size(); i++) {
                                HashMap map = (HashMap) list.get(i);


                                dataArrayList.add(new Alert(
                                        map.get("place").toString(),
                                        map.get("time").toString(),
                                        map.get("status").toString(),
                                        map.get("book_title").toString(),
                                        map.get("who").toString()
                                ));


                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                    else{
                        Log.d("TAG", "No such document");
                    }
                }
                else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}
