package com.example.booktobook.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.booktobook.Fragment.EnrollFragment;
import com.example.booktobook.Fragment.ProfileFragment;
import com.example.booktobook.R;
import com.example.booktobook.Fragment.SearchFragment;
import com.example.booktobook.Fragment.ShelfFragment;
import com.example.booktobook.Fragment.ShowFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private EnrollFragment enrollFragment = new EnrollFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private ShowFragment showFragment = new ShowFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private ShelfFragment shelfFragment = new ShelfFragment();
    Boolean flag;
    private Context context=this;

    private Intent serviceIntent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent!=null){
            stopService(serviceIntent);
            serviceIntent=null;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("pref",MODE_PRIVATE);
        final String ID = preferences.getString("ID","");
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        flag=false;
        firebaseFirestore.collection("Users")
                .document(ID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                flag=documentSnapshot.getBoolean("flag");
                if (flag)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("BookToBook");
                    builder.setMessage("책을 기부하였습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            flag=false;
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            flag=false;
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.getMenu().getItem(2).setChecked(true);

        //첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout,showFragment).commitAllowingStateLoss();

        //bottomNavigationView 의 아이템이 선택될 떄 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.navigation_enroll:{
                        transaction.replace(R.id.frame_layout,enrollFragment).commitAllowingStateLoss();
                        break;
                    }

                    case R.id.navigation_search:{
                        transaction.replace(R.id.frame_layout,searchFragment).commitAllowingStateLoss();
                        break;
                    }

                    case R.id.navigation_show:{
                        transaction.replace(R.id.frame_layout,showFragment).commitAllowingStateLoss();
                        break;
                    }

                    case R.id.navigation_profile:{
                        transaction.replace(R.id.frame_layout,profileFragment).commitAllowingStateLoss();
                        break;
                    }

                    case R.id.navigation_shelf:{
                        transaction.replace(R.id.frame_layout,shelfFragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                String newToken=instanceIdResult.getToken();
                Log.d("qwe","새 토큰"+newToken);

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                SharedPreferences preferences = getSharedPreferences("pref",MODE_PRIVATE);
                final String ID = preferences.getString("ID","");
                Log.d("id",ID);
                DocumentReference documentReference = db.collection("Users")
                        .document(ID);
                documentReference.update("token",newToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("dd","Sucessful update");
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }


}
