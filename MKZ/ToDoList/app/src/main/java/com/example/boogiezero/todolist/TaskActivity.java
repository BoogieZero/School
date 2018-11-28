package com.example.boogiezero.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.boogiezero.todolist.TaskList.Task;

import java.util.Calendar;
import java.util.Date;

/**
 * Activity providing controll for individual tasks.
 *  Editing attributes.
 *  Finish task. (remove)
 */
public class TaskActivity extends AppCompatActivity {

    /**
     * Stores when was button task done clicked.
     * Used for double click event.
     */
    private long lastClickTime = 0;

    /**
     * Actually processed task.
     *  Edited task or used as container for new task.
     */
    private Task actualTask;

    /**
     * Instance of internal data structure.
     */
    private TaskList taskList;

    /**
     * True if current window is used for editing, false for creating new task.
     */
    Boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //List
        taskList = TaskList.getInstance(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Intent
        Bundle b = getIntent().getExtras();
        editing = b.getBoolean("Edit");

        //Date, Time
        final EditText txDate = (EditText) findViewById(R.id.txDate_task);
        final EditText txTime = (EditText) findViewById(R.id.txTime_task);

        if(editing){
            //Edit existing
            int taskId = b.getInt("Task_id");
            actualTask = taskList.get(taskId);
            edit(txDate, txTime);
        }else{
            //Add new
            actualTask = TaskList.getDummy();
            add(txDate, txTime);
        }

        datePickerListener(txDate);
        timePickerListener(txTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(editing && taskList.isValidTask(actualTask)){
            //edditing valid task (task wasn't deleted (by Task Done)
            EditText tbDesc = (EditText) findViewById(R.id.tbDescription_task);
            String s = tbDesc.getText().toString();
            actualTask.description = s;
            taskList.update(actualTask.getId());
        }
    }

    /**
     * Fills apropriate elements of window with default values.
     * Changes title.
     * @param txDate    date text field
     * @param txTime    time text field
     */
    private void add(EditText txDate, EditText txTime) {
        //Button
        Button btDone = (Button)findViewById(R.id.btDone_task);
        btDone.setVisibility(View.GONE);

        //Date Time
        actualTask.date = Calendar.getInstance();
        txDate.setHint(actualTask.getDateString());
        txTime.setHint(actualTask.getTimeString());

        //Title
        setTitle(R.string.title_task_add);
    }

    /**
     * Fills appropriate values to window from task which is being editted.
     * @param txDate    date text field
     * @param txTime    time text field
     */
    private void edit(EditText txDate, EditText txTime) {
        //Button
        Button btAdd = (Button)findViewById(R.id.btAdd_task);
        btAdd.setVisibility(View.GONE);

        //Title
        String top =
                getResources().getString(R.string.title_task_edit) +" "+ actualTask.name;
        setTitle(top);

        //Name
        EditText txName = (EditText) findViewById(R.id.txName_task);
        txName.setEnabled(false);
        txName.setText(actualTask.name);

        //Date
        txDate.setText(actualTask.getDateString());

        //Time
        txTime.setText(actualTask.getTimeString());

        //Description
        EditText tbDesc = (EditText) findViewById(R.id.tbDescription_task);
        tbDesc.setText(actualTask.description);
    }

    /**
     * Sets listener on Time field for opening time picker.
     * Changes in time picker are propagated to actualTask.
     * @param txTime    time text field
     */
    private void timePickerListener(final EditText txTime) {
        //TimePicker for Time
        txTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener tot = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {


                        if(!editing){
                            actualTask.date = Calendar.getInstance();
                        }
                        actualTask.date.set(Calendar.HOUR_OF_DAY, hour);
                        actualTask.date.set(Calendar.MINUTE, minute);

                        String s = actualTask.getTimeString();
                        txTime.setText(s);
                    }
                };

                //On click
                Calendar c = Calendar.getInstance();
                TimePickerDialog t =
                        new TimePickerDialog(TaskActivity.this, tot,
                                c.get(Calendar.HOUR_OF_DAY),
                                c.get(Calendar.MINUTE),
                                true);
                t.show();

            }
        });
    }

    /**
     * Sets listener on Date field for openning datePicker.
     * Changes in datePicker are propagated to actualTask.
     * @param txDate    date text field
     */
    private void datePickerListener(final EditText txDate) {
        //DatePicker for Date
        txDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {


                        if(!editing){
                            actualTask.date = Calendar.getInstance();
                        }
                        actualTask.date.set(Calendar.YEAR, year);
                        actualTask.date.set(Calendar.MONTH, monthOfYear);
                        actualTask.date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String s = actualTask.getDateString();
                        txDate.setText(s);
                    }

                };

                //On click
                Calendar c = Calendar.getInstance();
                DatePickerDialog d =
                        new DatePickerDialog(TaskActivity.this, dpd,
                                c.get(Calendar.YEAR) ,
                                c.get(Calendar.MONTH),
                                c.get(Calendar.DAY_OF_MONTH));
                d.show();

            }
        });
    }

    /**
     * Fetches attributes from individual elements in edit window and tries
     * to create new Task.
     * On success task is added into internal structure therefore also into persistent database.
     * @param v view
     */
    public void addTask(View v){
        Task task = new Task();

        //Name
        EditText txName = (EditText) findViewById(R.id.txName_task);
        task.name = txName.getText().toString();
        if(task.name.trim().isEmpty()){
            String s = getResources().getString(R.string.txName_required_task);
            Toast.makeText(TaskActivity.this, s,
                    Toast.LENGTH_LONG).show();
            return;
        }

        //Date
        Date date;
        EditText txDate = (EditText) findViewById(R.id.txDate_task);
        try{
            date = Task.dateFormat.parse(txDate.getText().toString());

        }catch(Exception e){
            //Date missing -> today
            date = new Date();
        }
        task.date.setTime(date);

        //Time
        Date tDate = new Date();

        EditText txTime = (EditText) findViewById(R.id.txTime_task);
        boolean actualTime = false;
        try{
            tDate = Task.timeFormat.parse(txTime.getText().toString());
        }catch(Exception e){
            //Time missing -> actual time
            actualTime = true;
        }

        Calendar cTime = Calendar.getInstance();
        if(actualTime){
            task.date.set(Calendar.HOUR_OF_DAY,cTime.get(Calendar.HOUR_OF_DAY));
            task.date.set(Calendar.MINUTE,cTime.get(Calendar.MINUTE));
        }else{
            cTime.setTime(tDate);
            task.date.set(Calendar.HOUR_OF_DAY,cTime.get(Calendar.HOUR_OF_DAY));
            task.date.set(Calendar.MINUTE,cTime.get(Calendar.MINUTE));
        }

        //Description
        EditText tbDesc = (EditText) findViewById(R.id.tbDescription_task);
        String s = tbDesc.getText().toString();
        task.description = s;


        //Finish
        taskList.put(task);
        NavUtils.navigateUpFromSameTask(this);
    }

    /**
     * Button task done onDoubleClick event.
     * @param v view
     */
    public void taskDone_onDoubleClick(View v){
        long currentTime = System.currentTimeMillis();
        if((currentTime - lastClickTime) < ViewConfiguration.getDoubleTapTimeout()){
            taskDone(v);
        }else{
            lastClickTime = currentTime;
            String s = getResources().getString(R.string.btDone_doubleClick_task);
            Toast.makeText(TaskActivity.this, s,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Removes task from structures.
     * @param v view
     */
    public void taskDone(View v){
        taskList.remove(actualTask.getId());
        NavUtils.navigateUpFromSameTask(this);
    }
}
