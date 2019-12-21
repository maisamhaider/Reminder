package com.example.reminder.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.Fragments.TasksFrag;

import java.util.Calendar;
import java.util.Date;

import static com.example.reminder.classes.MyTimeSettingClass.todayPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.tomorrowPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.upcomingPlaceDate;

public class DataBaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Reminder.db";
    private static final String TABLE_NAME = "Day_List";
    private static final String ID = "ID";
    private static final String reminderText = "REMINDER_TEXT";
    private static final String reminder_date = "REMINDER_DATE";
    private static final String date_to_place_task = "DATE_TO_PLACE_TASK";


    public DataBaseHelper(@Nullable Context context) {
        super( context, DATABASE_NAME, null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( " create table "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY ,REMINDER_TEXT TEXT,REMINDER_DATE TEXT,DATE_TO_PLACE_TASK TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( " DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate( db );
    }

    public boolean insert(String text,String reminder_date,String date_to_place_task)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues(  );

        contentValues.put( reminderText,text );
        contentValues.put( this.reminder_date,reminder_date );
        contentValues.put( this.date_to_place_task,date_to_place_task );


        long result = database.insert( TABLE_NAME, null,contentValues );

        if (result == -1)
            return false;
            else
        return true;
    }

    public Cursor getToday() {
        SQLiteDatabase database = getWritableDatabase();
        String today = todayPlaceDate();

        Cursor day = database.rawQuery( " SELECT * FROM " + TABLE_NAME + " WHERE DATE_TO_PLACE_TASK LIKE \'" + today + "\'", null );


            return day;
    }
    public  Cursor getTomorrow()
    {
        SQLiteDatabase database = getWritableDatabase();
        Cursor day ;
        String tomorrow = tomorrowPlaceDate();
        day = database.rawQuery( "SELECT * FROM " + TABLE_NAME+" WHERE DATE_TO_PLACE_TASK LIKE \'"+tomorrow+"\'",null );
        return day;

    }
    public  Cursor getUpcoming()
    {
        SQLiteDatabase database = getWritableDatabase();
        Cursor day ;
        String upcoming = upcomingPlaceDate();
        day = database.rawQuery( "SELECT * FROM " + TABLE_NAME+" WHERE DATE_TO_PLACE_TASK LIKE \'"+upcoming+"\'",null );
        return day;

    }

}
