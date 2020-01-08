package com.example.reminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.reminder.classes.MyTimeSettingClass.todayPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.tomorrowPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.nextWeekPlaceDate;

public class DataBaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Reminder.db";
    private static final String TABLE_NAME = "Day_List";
    private static final String ID = "ID";
    private static final String reminderText = "REMINDER_TEXT";
    private static final String reminder_date = "REMINDER_DATE";
    private static final String date_to_place_task = "DATE_TO_PLACE_TASK";
    private static final String sub_tasks = "SUB_TASKS";
    private static final String task_notes = "TASK_NOTES";
    private static final String task_created_date = "CREATED_DATE";



    public DataBaseHelper(@Nullable Context context) {
        super( context, DATABASE_NAME, null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( " create table "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT ,REMINDER_TEXT TEXT,REMINDER_DATE TEXT" +
                ",DATE_TO_PLACE_TASK TEXT,SUB_TASKS TEXT,TASK_NOTES TEXT,CREATED_DATE TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( " DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate( db );
    }

    public boolean insert(String text,String reminder_date,String date_to_place_task,String createdDate)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues(  );
        contentValues.put( reminderText,text );
        contentValues.put( this.reminder_date,reminder_date );
        contentValues.put( this.date_to_place_task,date_to_place_task );
        contentValues.put( task_created_date,createdDate );

        long result = database.insert( TABLE_NAME, null,contentValues );

        if (result == -1)
            return false;
            else
        return true;
    }
    public boolean insert(String subTask,String notes)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues(  );
        contentValues.put( sub_tasks,subTask );
        contentValues.put( task_notes,notes );

        long result = database.insert( TABLE_NAME,null,contentValues );
        if (result == -1)
            return false;
            else
                return true;
    }

    public boolean updateNotesColumn(String notes,String position)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues(  );
        contentValues.put( task_notes,notes );

       long isUpdate = database.update( TABLE_NAME,contentValues,"ID=?",new String[]{position} );

       if (isUpdate==-1)
       {
           return false;
       }
        return true;
    }

    public Cursor getToday() {
        SQLiteDatabase database = getWritableDatabase();
        String today = todayPlaceDate();

        Cursor day = database.rawQuery( " SELECT * FROM " + TABLE_NAME+ " WHERE DATE_TO_PLACE_TASK LIKE \'"+ today +"\'", null );
            return day;
    }
    public  Cursor getTomorrow()
    {
        SQLiteDatabase database = getWritableDatabase();
        Cursor day ;
        String tomorrow = tomorrowPlaceDate();
        day = database.rawQuery( "SELECT * FROM " + TABLE_NAME+ " WHERE DATE_TO_PLACE_TASK LIKE \'"+ tomorrow +"\'",null );
        return day;

    }
    public  Cursor getUpcoming()
    {
        SQLiteDatabase database = getWritableDatabase();
        Cursor day ;
        day = database.rawQuery( "SELECT * FROM " + TABLE_NAME,null,null );
        return day;

    }
    public Cursor getSomeday()
    {
        SQLiteDatabase database = getReadableDatabase();
        Cursor day;
        String someday = "";
        day = database.rawQuery( "SELECT * FROM "+TABLE_NAME+" WHERE DATE_TO_PLACE_TASK LIKE \'"+someday+"\'",null);
        return day;
    }

    public Cursor getSubTasks(String position)
    {
        SQLiteDatabase database = getWritableDatabase();
        Cursor STC = database.rawQuery( "SELECT SUB_TASKS FROM "+TABLE_NAME+" WHERE ID LIKE \'"+position+"\'",null );
        return STC;
    }

    public Cursor getTaskNote(String position)
    {
        SQLiteDatabase database = getWritableDatabase();
      Cursor TNC = database.rawQuery( "SELECT TASK_NOTES FROM "+TABLE_NAME+" WHERE ID LIKE \'"+position+"\'",null );
        return TNC;
    }


    public Cursor getCreatedDate(String position)
    {
        SQLiteDatabase database = getWritableDatabase();
       Cursor CDC = database.rawQuery("SELECT CREATED_DATE FROM "+TABLE_NAME+" WHERE ID LIKE \'"+position+"\'",null );
        return CDC;
    }


    public void deleteOneItem(String position)
    {
        SQLiteDatabase database = getWritableDatabase();
        int DOT = database.delete( TABLE_NAME, "ID=?", new String[]{String.valueOf(position)} );

    }

    public void deleteSubTask(String position)
    {
        SQLiteDatabase database = getWritableDatabase();
        int DST = database.delete( "DELETE FROM "+TABLE_NAME," WHERE ID=?",new String[]{String.valueOf( position )} );

    }


}
