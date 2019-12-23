package com.example.booktobook.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.booktobook.Activity.AlertActivity;
import com.example.booktobook.Model.User;
import com.example.booktobook.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    public ImageButton profilefragment_alert;
    ImageView imageView;
    File tempFile;

    public TextView fragment_profile_textview_name;
    public TextView fragment_profile_textview_point;
    public TextView fragment_profile_textview_uploaded_book;
    public TextView fragment_profile_textview_borrow_book;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100)
        {
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try {
            String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        }

    }

    private void setImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        imageView.setImageBitmap(originalBm);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        profilefragment_alert = view.findViewById(R.id.button_alert);
        imageView=view.findViewById(R.id.imageview_profile);

        fragment_profile_textview_name = view.findViewById(R.id.fragment_profile_name);
        fragment_profile_textview_point = view.findViewById(R.id.fragment_profile_point);
        fragment_profile_textview_borrow_book = view.findViewById(R.id.fragment_profile_borrow_book);
        fragment_profile_textview_uploaded_book = view.findViewById(R.id.fragment_profile_upload_book);

        profilefragment_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AlertActivity.class);
                startActivity(intent);
            }
        });


        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        final String id = pref.getString("ID", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        fragment_profile_textview_name.setText(""+id);

        DocumentReference docRef = db.collection("Users").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                fragment_profile_textview_point.setText(""+user.getPoint());
                fragment_profile_textview_borrow_book.setText(""+user.getBorrowed_book_count());
                fragment_profile_textview_uploaded_book.setText(""+user.getUploaded_book_count());
            }
        });




        return view;
    }

    View.OnClickListener cameraListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {


        }
    };
    View.OnClickListener galleryListener= new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            getActivity().startActivityForResult(intent,100);


        }
    };



}
