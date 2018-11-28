package com.example.boogiezero.todolist;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;

import android.content.Intent;

import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.List;


/**
 * Service class creating notification reports.
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    /**
     * Creates notification with report based on number of Tasks scheduled for today
     * and bumber of all Tasks.
     * @param intent intent for this service
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        TaskList taskList = TaskList.getInstance(this);
        List<TaskList.Task> sorted = taskList.getSortedList();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 24);
        long time = c.getTimeInMillis();

        int count = 0;
        for (TaskList.Task task: sorted) {
            if(task.date.getTimeInMillis() - time < 1000*60*60*24){
                count ++;
            }
        }

        String text = getResources().getString(R.string.notification_text);
        String result = String.format(text, count, sorted.size());
        String title = getResources().getString(R.string.notification_title);

        if(count == 0)return;

        int N_ID = 21;
        Notification n;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        n = builder
                .setContentTitle(title)
                .setContentText(result)
                .setSmallIcon(R.drawable.ic_stat_name)
                .build();
        n.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify(N_ID, n);
    }
}
