package com.example.macbook.smartparking.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.macbook.smartparking.R;

/**
 * Created by skirk on 9/11/17.
 */

public class NotificationCancelReceiver extends BroadcastReceiver {

    static final String cancelAction = "com.example.cancel";
    static final String acceptAction = "com.example.accept";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Cancel your ongoing Notification
        if (intent.getAction().equals(cancelAction)) {
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.cancel(R.string.local_service_started);
        }
        if(intent.getAction().equals(acceptAction)){
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.cancel(R.string.local_service_started);
        }
    }

    ;
}