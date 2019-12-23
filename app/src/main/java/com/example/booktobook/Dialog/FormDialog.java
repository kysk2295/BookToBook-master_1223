package com.example.booktobook.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booktobook.Interface.CustomDialogListener;
import com.example.booktobook.R;


public class FormDialog extends Dialog {
    private TextView mPositiveButton;
    private TextView mNegativeButton;
    private TextView sender,receiver,time,location,alarm;
    private ImageView x;
    private CustomDialogListener customDialogListener;
    private View.OnClickListener mNegativeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.formdialog);

        mPositiveButton=findViewById(R.id.textview_ok);
        mNegativeButton=findViewById(R.id.textview_cancel);
        sender=findViewById(R.id.textview_sender);
        receiver=findViewById(R.id.textview_receiver);
        time=findViewById(R.id.textview_time);
        location=findViewById(R.id.textview_place);
        alarm=findViewById(R.id.textview_alarm);
        x=findViewById(R.id.imageview_wrong);



        mNegativeButton.setOnClickListener(mNegativeListener);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialogListener.onTimeClicked();

            }
        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.onNegativeClicked();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialogListener.onLocationClicked();
            }
        });
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.onPostiveClicked(sender.getText().toString(),receiver.getText().toString(),time.getText().toString()
                ,location.getText().toString(),alarm.getText().toString());
            }
        });





    }

    public FormDialog(Context context, View.OnClickListener negativelistener)
    {
        super(context);
        mNegativeListener=negativelistener;


    }

    public void setText(String s1,String s2) {
        Log.d("iam1",s1);
        sender.setText(s1);
        receiver.setText(s2);
    }
    public void setTimeText(String s){
        time.setText(s);
    }
    public void setLocationText(String s){location.setText(s);}

    public void setDialogListner(CustomDialogListener customDialogListener){
        this.customDialogListener=customDialogListener;
    }
}
