package com.example.booktobook.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.booktobook.Activity.ChatActivity;
import com.example.booktobook.Activity.RankActivity;
import com.example.booktobook.Adapter.AdapterBook;
import com.example.booktobook.Model.BookData;
import com.example.booktobook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ShowFragment extends Fragment{

    private RecyclerView recyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    public ImageButton rankButton,chatButton;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<BookData> dataArrayList;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show,container,false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view_show);
        swipeRefreshLayout = view.findViewById(R.id.refresh_showfragment);
        rankButton = view.findViewById(R.id.button_rank);
        chatButton=view.findViewById(R.id.chatBtn);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);
            }
        });

        rankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RankActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.invalidate();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //use a grid layout manager
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        final String id = pref.getString("ID", "");


        dataArrayList = new ArrayList<>();
        adapter = new AdapterBook(dataArrayList,getContext());
        recyclerView.setAdapter(adapter);



        db.collection("Books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("ShowFragment", document.getId() + " => " + document.getData());
                                if(document.getBoolean("abled")){

                                    if(!document.get("haver").equals(id)){

                                        Log.d("kwk_haver", (String) document.get("haver"));
                                        Log.d("kwk_id", id);

                                        dataArrayList.add(new BookData(
                                                document.get("book_image").toString(),
                                                document.get("title").toString(),
                                                "저자:"+document.get("author").toString(),
                                                "출판사:"+document.get("publisher").toString(),
                                                "주인:"+document.get("haver").toString()
                                        ));
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("ShowFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });





        return view;
    }



}

