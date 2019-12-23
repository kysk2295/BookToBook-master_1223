package com.example.booktobook.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getStringExtra("action");
        if (action.equals("cancel")){
            Log.d("fuck","cancel");

        }else if(action.equals("confirm"))
        {

        }
    }
}
