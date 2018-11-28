package com.example.boogiezero.todolist;

import android.content.Context;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents internal data structure and provides tool for access and updates.
 * Used data are automatically propagated into persistent database.
 * Works by Singleton pattern (database included).
 */
public class TaskList {
    /**
     * Dummy Task.
     * Can be used as a container for temporary data.
     */
    private static Task dummy;

    /**
     * Only instance of this class.
     */
    private static TaskList instance;

    /**
     * Encapsulation of actual persistent database.
     */
    private static DBHelper db;

    /**
     * Contains all tasks by it's id as key.
     */
    private static Map<Integer, Task> map;

    private static int lowestId;    //lowest id available

    /**
     * Highest id assigned to task.
     */
    private static int highestId;   //highest id assigned

    /**
     * Returns dummy task.
     * @return
     */
    public static Task getDummy(){
        return dummy;
    }

    /**
     * Gets the only instace of this class.
     * Instance is created for the first time (including database).
     * @param context   context for database
     * @return          only instance of this class
     */
    public static TaskList getInstance(Context context) {
        if(instance == null){
            instance = new TaskList(context);
        }
        db = new DBHelper(context);
        return instance;
    }

    /**
     * Returns task by given id.
     * @param id    task id
     * @return      task with given id
     */
    public Task get(int id){
        return map.get(id);
    }

    /**
     * Inserts new task into data structure.
     * @param task new task
     */
    public void put(Task task){
        int id = task.getId();
        map.put(task.getId(), task);
        if(id > highestId) highestId = id;
        if(id == lowestId){
            while(true){
                lowestId++;
                if(lowestId == highestId || !map.containsKey(lowestId)){
                    break;
                }
            }
        }
        db.insertTask(task.getId(), task.name, task.date.getTimeInMillis(), task.description);
    }

    /**
     * Removes task with given id.
     * @param id    task id
     */
    public void remove(int id){
        db.deleteTask(id);
        map.remove(id);
        if(id<lowestId) lowestId = id;
    }

    /**
     * Returns sorted list of all tasks (by date).
     * @return  sorted tasks
     */
    public List<Task> getSortedList(){
        ArrayList<Task> list = new ArrayList<Task>(map.values());
        Collections.sort(list);
        return (List<Task>)list.clone();
    }

    /**
     * Create new and only instance of this class and DBHelper.
     * @param context
     */
    private TaskList(Context context) {
        map = new HashMap<Integer, Task>();

        //create dummy
        lowestId = -1;
        highestId = -1;
        dummy = new Task();

        //setup
        lowestId = 0;
        highestId = 0;

        populateFromDB(context);
    }

    /**
     * Populates data structures from database.
     * @param context   context for creation of the database
     */
    private void populateFromDB(Context context) {
        db = new DBHelper(context);
        int rows = db.numberOfRows();
        if(db.numberOfRows() < 1){
            //Empty database
            return;
        }

        int id;
        String name;
        long dateL;
        String description;

        Cursor cursor = db.getAllTasks();
        Task newTask;
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
            name = cursor.getString(1);
            dateL = cursor.getLong(2);
            description = cursor.getString(3);
            newTask = new Task(
                    id,
                    name,
                    dateL,
                    description);
            map.put(id, newTask);
            if(id > highestId) highestId = id;
        }
        cursor.close();

    }

    /**
     * Generates id for a new task.
     * @return  generated id
     */
    private static int generateId(){
        for (int i = lowestId; i <= highestId; i++){
            if(!map.containsKey(i)) return i;
        }
        return highestId + 1;
    }

    /**
     * Checks if given task is valid.
     * Task is not null, and is present in data structures.
     * @param task  examined task
     * @return  true for valid task
     */
    public boolean isValidTask(Task task){
        if(task == null || task == dummy){
            return false;
        }
        return map.containsKey(task.getId());
    }

    /**
     * Propagates changes in task with given id into persistent database.
     * @param id    task id
     */
    public void update(int id) {
        Task task = map.get(id);
        db.updateTask(id, task.name, task.date.getTimeInMillis(), task.description);
    }

    /**
     * Represents single task with it's attributes and provides tool for output of it's attributes.
     */
    public static class Task implements Comparable<Task>{
        /**
         * Format for remaining time.
         */
        public static SimpleDateFormat remainingFormat = new SimpleDateFormat("");

        /**
         * Format for due date.
         */
        public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        /**
         * Format for due time.
         */
        public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        /**
         * Taks id (unique).
         */
        private int id;

        /**
         * Name.
         */
        public String name;

        /**
         * Due date.
         */
        public Calendar date;

        /**
         * Description.
         */
        public String description;

        /**
         * Returns remaining time to due date in the form of Callendar.
         * @return remaining time to due date
         */
        public Calendar getRemainingTime(){
            Calendar c = Calendar.getInstance();
            long diference = date.getTimeInMillis() - c.getTimeInMillis();
            double dd = diference / (60.0*60.0*24.0*1000.0);
            c.setTimeInMillis(diference);

            return c;
        }

        /**
         * Creates formatted string with remaining time to due date.
         * @return formatted remaining time
         */
        public String getRemainingTimeString(){
            Calendar c = getRemainingTime();
            double dd = c.getTimeInMillis() / (60.0*60.0*24.0*1000.0);
            int d = (int)dd;
            double hd = (dd-d)*24.0;
            int h = (int)hd;

            if(dd<0) return "["+getDateString()+"]";

            String out = d+"d ";
            if(d<7) out+= h+"h";

            return out;
        }

        /**
         * Returns formatted date.
         * @return formatted date
         */
        public String getDateString(){
            String s = dateFormat.format(date.getTime());
            return s;
        }

        /**
         * Returns formatted time.
         * @return  formatted time
         */
        public String getTimeString(){
            String s = timeFormat.format(date.getTime());
            return s;
        }

        /**
         * Creates new default task.
         * date = actual date and time
         */
        public Task(){
            this.id = generateId();
            this.date = Calendar.getInstance();
        }

        /**
         * Creates new default task with given name
         * @param name  taks name
         */
        public Task(String name){
            this();
            this.name = name;
        }

        /**
         * Creates new task from given parameters.
         * @param id            task id
         * @param name          task name
         * @param date          task due date
         * @param description   task description
         */
        public Task(int id, String name, long date, String description){
            this.id = id;
            this.name = name;
            this.date = Calendar.getInstance();
            this.date.setTimeInMillis(date);
            this.description = description;
        }

        @Override
        public String toString(){
            return name + " "+getRemainingTimeString();
        }

        /**
         * Returns task id
         * @return taks id
         */
        public int getId() {
            return id;
        }

        @Override
        public int compareTo(Task t) {
            return this.date.compareTo(t.date);
        }
    }

}

