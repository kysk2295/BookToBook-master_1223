package com.example.booktobook.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.booktobook.Adapter.AdapterUser;
import com.example.booktobook.Fragment.PointFragment;
import com.example.booktobook.R;
import com.example.booktobook.Fragment.ReadFragment;
import com.example.booktobook.Fragment.UploadFragment;
import com.example.booktobook.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {
    public ImageButton rankBackButton;

    public FragmentManager fragmentManager = getSupportFragmentManager();

    public PointFragment pointFragment = new PointFragment();
    public ReadFragment readFragment = new ReadFragment();
    public UploadFragment uploadFragment = new UploadFragment();
    ArrayList<User> userArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    AdapterUser adapterUser = new AdapterUser(userArrayList);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        rankBackButton = findViewById(R.id.activity_rank_back_button);
        rankBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_rank_bottom_navigation_view);

        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        //첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.activity_rank_frame_layout,pointFragment).commitAllowingStateLoss();

        //bottomNavigationView 의 아이템이 선택될 떄 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.navigation_point:{
//
//                        transaction.replace(R.id.frame_layout,pointFragment).commitAllowingStateLoss();
                        replaceFragment(pointFragment);
                        return true;
                    }

                    case R.id.navigation_read:{
//                        transaction.replace(R.id.frame_layout,readFragment).commitAllowingStateLoss();
                        replaceFragment(readFragment);
                        return true;
                    }

                    case R.id.navigation_upload:{
//                        transaction.replace(R.id.frame_layout,uploadFragment).commitAllowingStateLoss();
                        replaceFragment(uploadFragment);
                        return true;
                    }
                }

                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_rank_frame_layout, fragment).commitAllowingStateLoss();
    }
}
