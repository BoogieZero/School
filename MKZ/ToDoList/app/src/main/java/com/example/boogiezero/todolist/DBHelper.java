package com.example.boogiezero.todolist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class encapsulating persistent database and provides tool for accesing it.
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * Database name.
     */
    public static final String DATABASE_NAME = "DBList.db";

    /**
     * ID column.
     */
    public static final String TASK_COLUMN_ID = "id";

    /**
     * NAME column.
     */
    public static final String TASK_COLUMN_NAME = "name";

    /**
     * DATE column
     */
    public static final String TASK_COLUMN_DATE = "date";

    /**
     * DESCRIPTION column
     */
    public static final String TASK_COLUMN_DESCRIPTION = "description";

    /**
     * Instantiates database.
     * Populates database from the persistent one (based on given context).
     * @param context context for database
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table tasks " +
                        "(id integer primary key, name text, date long, description text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    /**
     * Inserts new Task given by parameters into database.
     * @param id            task id
     * @param name          task name
     * @param date          task due date
     * @param description   task description
     * @return              true for successful insert
     */
    public boolean insertTask (int id, String name, long date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("date", date);
        contentValues.put("description", description);
        db.insert("tasks", null, contentValues);
        return true;
    }

    /**
     * Returns cursor on task row with given id.
     * @param id    task id
     * @return      cursor on task row with given id
     */
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from tasks where id="+id+"", null );
        return res;
    }

    /**
     * Returns cursor on select over all tasks.
     * @return  cursor on "select * taska"
     */
    public Cursor getAllTasks(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from tasks", null );
        return res;
    }

    /**
     * Returns number of tasks in database.
     * @return number of tasks in database.
     */
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "tasks");
        return numRows;
    }

    /**
     * Updates data for task given by parameters.
     * @param id            task id
     * @param name          task name
     * @param date          task due date
     * @param description   task description
     * @return              true for success
     */
    public boolean updateTask(Integer id, String name, long date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("date", date);
        contentValues.put("description", description);
        db.update("tasks", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    /**
     * Removes task with given id from database.
     * @param id    task id
     * @return
     */
    public void deleteTask(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tasks", "id = ? ", new String[] { Integer.toString(id) });
    }

}
