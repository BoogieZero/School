package com.example.boogiezero.todolist;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ToggleButton;
import java.util.Calendar;

/**
 * Main activity. Displays sorted list which can be edited by click action on elements.
 * New element can be added by button.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Adapter for main listview for populating by data.
     */
    private ListArrayAdapter adapter;

    /**
     * Sorted list of tasks.
     */
    private ListView listView;

    /**
     * Alarm for periodical notifications.
     */
    private static AlarmManager alarm;

    /**
     * Intent for alarm (notification)
     */
    private static Intent intent;

    private static boolean notifications = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //List
        TaskList tdList = TaskList.getInstance(this);

        //ListView
        adapter = new ListArrayAdapter(this, android.R.layout.simple_list_item_1, tdList.getSortedList());
        listView = (ListView) findViewById(R.id.list_main);
        listView.setAdapter(adapter);
        setListener(listView);

        //Toggle
        ToggleButton toggle = (ToggleButton)findViewById(R.id.toggleButton);
        toggle.setChecked(notifications);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Sets listener onClick for items in listView.
     * @param listView source listView
     */
    private void setListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TaskList.Task t = (TaskList.Task)adapterView.getItemAtPosition(position);
                Intent open = new Intent(MainActivity.this, TaskActivity.class);
                open.putExtra("Edit", true);
                open.putExtra("Task_id", t.getId());
                startActivity(open);
            }
        });
    }

    /**
     * onClick event for button add. Starts new activity with edit window.
     * @param v view
     */
    public void btAdd_onClick(View v){
        Intent open = new Intent(MainActivity.this, TaskActivity.class);
        open.putExtra("Edit", false);

        startActivity(open);
    }

    /**
     * Button notifications onClick event.
     * @param view view
     */
    public void btToggle_onClick(View view) {
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            //Enable notifications
            scheduleNotifications();
        } else {
            //Disable notifications
            cancelNotifications();
        }
    }

    /**
     * Schedule notifications.
     * Sets AlarmManager and NotificationService receiver for daily notification popup.
     */
    public void scheduleNotifications(){
        intent = new Intent(getApplicationContext(), NotificationServiceReceiver.class);
        //Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent =
                PendingIntent.getBroadcast(
                        this,
                        NotificationServiceReceiver.REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        //Start time
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        long firstMillis = c.getTimeInMillis();

        if(alarm == null){
            alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarm.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    firstMillis,
                    AlarmManager.INTERVAL_DAY,
                    pIntent);
        }
        notifications = true;
    }

    /**
     * Stops AlarmManager intent for periodical notifications.
     */
    public void cancelNotifications() {
        //Intent intent = new Intent(getApplicationContext(), NotificationService.class);
        final PendingIntent pIntent =
                PendingIntent.getBroadcast(
                        this,
                        NotificationServiceReceiver.REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        alarm.cancel(pIntent);
        alarm = null;
        notifications = false;
    }

    /**
     * Checks if service is running.
     * @param serviceClass service class
     * @return true if given service is running
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}

