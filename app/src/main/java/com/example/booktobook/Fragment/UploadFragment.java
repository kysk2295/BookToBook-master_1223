package com.example.booktobook.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.booktobook.Adapter.AdapterUser;
import com.example.booktobook.Model.User;
import com.example.booktobook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class UploadFragment extends Fragment {

    public RecyclerView recyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;
    public ArrayList<User> dataArrayList;
    public String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank,container,false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerView  = view.findViewById(R.id.recycler_view_rank);

        swipeRefreshLayout = view.findViewById(R.id.refresh_shelffragment);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        //get id from sharedPreference
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        id = pref.getString("ID", "");


        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        dataArrayList = new ArrayList<>();
        adapter = new AdapterUser(dataArrayList);
        recyclerView.setAdapter(adapter);


        //  수정: User의 myBooks를 삭제하고 그냥 Books에서 haver가 나 인걸 가져오자

        db.collection("Users")
                .orderBy("uploaded_book_count", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                dataArrayList.add(new User(
                                        document.get("id").toString(),
                                        document.get("password").toString(),
                                        Integer.parseInt(String.valueOf(document.get("point"))),
                                        Integer.parseInt(String.valueOf(document.get("uploaded_book_count"))),
                                        Integer.parseInt(String.valueOf(document.get("borrowed_book_count")))
                                ));


                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return view;
    }
}
