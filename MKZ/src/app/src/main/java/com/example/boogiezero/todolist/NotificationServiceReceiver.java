package com.example.boogiezero.todolist;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class NotificationServiceReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 78921;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NotificationService.class);
        //i.putExtra("foo", "bar");
        Log.d("RECEIVER", "Starting service");
        context.startService(i);
    }


}
