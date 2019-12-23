package com.example.booktobook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booktobook.Adapter.AdapterBook;
import com.example.booktobook.Model.BookData;
import com.example.booktobook.R;
import com.example.booktobook.Fragment.SearchFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends Activity implements SearchFragment.OnDataPassListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<BookData> dataArrayList = new ArrayList<>();
    private String data;
    private ImageButton back_button;
    private TextView input_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_from_search);

        recyclerView = findViewById(R.id.recycler_view_search);
        back_button = findViewById(R.id.back_button);
        input_title = findViewById(R.id.input_title_text);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        Intent i = getIntent();
        data = i.getExtras().getString("abc");
        Log.d("tag", data);
        input_title.setText(data.toString());


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference BOOKS = db.collection("Books");

        //firestore.collection("Books").whereEqualTo(data,true)
        BOOKS.orderBy("title")
                .startAt(data)
                .endAt(data + "\uf8ff")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            BookData bookData = documentSnapshot.toObject(BookData.class);
                            if(bookData.abled == true) {
                                dataArrayList.add(new BookData(
                                        bookData.getBook_image().toString(),
                                        bookData.getTitle().toString(),
                                        "저자:" + bookData.getAuthor().toString(),
                                        "출판사:" + bookData.getPublisher().toString(),
                                        "주인:" + bookData.getHaver().toString()
                                ));

                                if (bookData.getTitle().equals("null")){
                                    input_title.setText("검색 결과가 나오지 않습니다.");
                                }
                            }


                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //set adapter
        SharedPreferences pref = this.getSharedPreferences("pref", MODE_PRIVATE);
        String id = pref.getString("ID", "");


        adapter = new AdapterBook(dataArrayList,this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public String onDataPass(String data) {
        this.data = data;
        return this.data;
    }
}