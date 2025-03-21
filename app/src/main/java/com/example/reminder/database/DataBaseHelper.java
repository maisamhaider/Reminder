package com.example.reminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.reminder.utilities.MyTimeSettingClass.todayPlaceDate;
import static com.example.reminder.utilities.MyTimeSettingClass.tomorrowPlaceDate;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Task table
    private static final String DATABASE_NAME = "Reminder.db";
    private static final String TABLE_NAME = "Day_List";
    private static final String ID = "ID";
    private static final String reminderText = "REMINDER_TEXT";
    private static final String reminder_date = "REMINDER_DATE";
    private static final String date_to_place_task = "DATE_TO_PLACE_TASK";
    private static final String task_notes = "TASK_NOTES";
    private static final String task_created_date = "CREATED_DATE";
    private static final String completed_tasks = "COMPLETED_TASK";
    private static final String repeat = "REPETITION";
    private static final String isAlarmOn = "IS_ALARM_ON";
    private static final String intervalTime = "INTERVAL_TIME";


    // sub task table

    private static final String SUB_TASK_TABLE = "Sub_Task_Table";
    private static final String SUB_TASKS = "SUB_TASKS";
    private static final String SUB_TASK_PID = "P_ID";
    private static final String SUB_TASKS_FID = "F_ID";

    // FILE TABLE
    private static final String IMAGE_TABLE = "image_table";
    private static final String IMAGE = "IMAGE";
    private static final String FILE_CREATED_DATE = "FILE_CREATED_DATE";
    private static final String IMAGE_PID = "IMAGE_P_KEY";
    private static final String IMAGE_FID = "IMAGE_F_KEY";


    public DataBaseHelper(@Nullable Context context) {
        super( context, DATABASE_NAME, null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( " create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT ,REMINDER_TEXT TEXT,REMINDER_DATE TEXT" +
                ",DATE_TO_PLACE_TASK TEXT,TASK_NOTES TEXT,CREATED_DATE TEXT,COMPLETED_TASK TEXT,REPETITION TEXT,IS_ALARM_ON TEXT,INTERVAL_TIME TEXT)" );
        db.execSQL( "create table " + SUB_TASK_TABLE + "(P_ID INTEGER PRIMARY KEY AUTOINCREMENT,SUB_TASKS TEXT,F_ID TEXT)" );
        db.execSQL( "create table " + IMAGE_TABLE + "(IMAGE_P_KEY INTEGER PRIMARY KEY AUTOINCREMENT,IMAGE_F_KEY TEXT,IMAGE TEXT,FILE_CREATED_DATE TEXT)" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( " DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate( db );
    }

    public int insert(String text, String reminder_date, String date_to_place_task, String createdDate, String repeat, String isAlarmOn) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put( reminderText, text );
        contentValues.put( DataBaseHelper.reminder_date, reminder_date );
        contentValues.put( DataBaseHelper.date_to_place_task, date_to_place_task );
        contentValues.put( task_created_date, createdDate );
        contentValues.put( DataBaseHelper.repeat, repeat );
        contentValues.put( DataBaseHelper.isAlarmOn, isAlarmOn );
        int position;

        long result = database.insert( TABLE_NAME, null, contentValues );
        Cursor cursor = getRowWhere( createdDate );
        if (cursor != null && cursor.getCount() != 0) {

            while (cursor.moveToNext()) {
                position = cursor.getInt( 0 );
                return position;

            }
        }
        return 0;
    }

    public boolean upDate(String key, String isCompleted, String isAlarmOn) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( completed_tasks, isCompleted );
        contentValues.put( DataBaseHelper.isAlarmOn, isAlarmOn );
        long result = database.update( TABLE_NAME, contentValues, "ID=?", new String[]{key} );

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean upDate(String key, String intervalTime) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBaseHelper.intervalTime, intervalTime );
        long result = database.update( TABLE_NAME, contentValues, "ID=?", new String[]{key} );

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateReminderTime(String key, String reminderTime) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( reminder_date, reminderTime );
        long result = database.update( TABLE_NAME, contentValues, "ID=?", new String[]{key} );

        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insertSubTask(String sub_tasks, String F_KEY) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put( SUB_TASKS, sub_tasks );
        cv.put( SUB_TASKS_FID, F_KEY );

        long result = database.insert( SUB_TASK_TABLE, null, cv );
        if (result == -1) {
            return false;
        } else
            return true;

    }

    public boolean insertFile(String filePath, String c_date, String fk) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put( IMAGE, filePath );
        cv.put( FILE_CREATED_DATE, c_date );
        cv.put( IMAGE_FID, fk );


        long result = database.insert( IMAGE_TABLE, null, cv );
        if (result == -1) {
            return false;
        } else
            return true;

    }


    public boolean update(String reminder_date, String date_to_place_task, String repeat, String isAlarmOn, String position) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBaseHelper.reminder_date, reminder_date );
        contentValues.put( DataBaseHelper.date_to_place_task, date_to_place_task );
        contentValues.put( DataBaseHelper.repeat, repeat );
        contentValues.put( DataBaseHelper.isAlarmOn, isAlarmOn );

        long result = database.update( TABLE_NAME, contentValues, "ID=?", new String[]{position} );

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateTitle(String title, String position) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( reminderText, title );

        long result = database.update( TABLE_NAME, contentValues, "ID=?", new String[]{position} );

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean update(String reminder_date, String position) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBaseHelper.reminder_date, reminder_date );
        contentValues.put( DataBaseHelper.isAlarmOn, isAlarmOn );

        long result = database.update( TABLE_NAME, contentValues, "ID=?", new String[]{position} );

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateNotesColumn(String notes, String position) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( task_notes, notes );

        long isUpdate = database.update( TABLE_NAME, contentValues, "ID=?", new String[]{position} );

        if (isUpdate == -1) {
            return false;
        }
        return true;
    }

//    public boolean updateRepeatRow(String position, String repeat, String reminder_date, String date_to_place_task ) {
//        SQLiteDatabase database = getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put( DataBaseHelper.repeat, repeat );
//        contentValues.put( DataBaseHelper.reminder_date, reminder_date );
//        contentValues.put( DataBaseHelper.date_to_place_task, date_to_place_task );
//
//        long isUpdate = database.update( TABLE_NAME, contentValues, "ID=?", new String[]{position} );
//
//        if (isUpdate == -1) {
//            return false;
//        }
//        return true;
//    }

    public Cursor getAllTasks() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor allTasks;
        allTasks = database.rawQuery( "SELECT * FROM " + TABLE_NAME, null, null );
        return allTasks;

    }

    public Cursor getSingleRow(String p) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor row;
        row = database.rawQuery( "SELECT * FROM " + TABLE_NAME + " WHERE ID LIKE \'" + p + "\'", null );
        return row;

    }

    public Cursor getToday() {
        SQLiteDatabase database = getWritableDatabase();
        String today = todayPlaceDate();

        Cursor day = database.rawQuery( " SELECT * FROM " + TABLE_NAME + " WHERE DATE_TO_PLACE_TASK LIKE \'" + today + "\'", null );
        return day;
    }

    public Cursor getTomorrow() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor day;
        String tomorrow = tomorrowPlaceDate();
        day = database.rawQuery( "SELECT * FROM " + TABLE_NAME + " WHERE DATE_TO_PLACE_TASK LIKE \'" + tomorrow + "\'", null );
        return day;

    }

    public Cursor getUpcoming() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor day;
        day = database.rawQuery( "SELECT * FROM " + TABLE_NAME, null, null );
        return day;

    }

    public Cursor getSomeday() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor day;
        String someday = "";
        day = database.rawQuery( "SELECT * FROM " + TABLE_NAME + " WHERE DATE_TO_PLACE_TASK LIKE \'" + someday + "\'", null );
        return day;
    }

    public Cursor getDateToPlaceSingleRowValue(String position) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor singleDateToPlaceRow;
        singleDateToPlaceRow = database.rawQuery( "SELECT DATE_TO_PLACE_TASK FROM " + TABLE_NAME + " WHERE ID LIKE \'" + position + "\'", null );
        return singleDateToPlaceRow;
    }

    //    public Cursor getRepeatColumnValue()
//    {
//        SQLiteDatabase database = getWritableDatabase();
//        Cursor repeatRow;
//        repeatRow = database.rawQuery( "SELECT * FROM " + TABLE_NAME, null, null );
//        return repeatRow;
//
//    }
    public Cursor getSingleRepeatRowValue(String position) {
        SQLiteDatabase database = getWritableDatabase();
        return  database.rawQuery( "SELECT REPETITION FROM " + TABLE_NAME + " WHERE ID LIKE \'" + position + "\'", null );

    }


    public Cursor getSubTasks(String position) {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery( "SELECT * FROM " + SUB_TASK_TABLE + " WHERE F_ID LIKE \'" + position + "\'", null );
    }

    public Cursor getTaskNote(String position) {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery( "SELECT TASK_NOTES FROM " + TABLE_NAME + " WHERE ID LIKE \'" + position + "\'", null );
    }

    public Cursor getRowWhere(String date) {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery( "SELECT ID FROM " + TABLE_NAME + " WHERE CREATED_DATE LIKE \'" + date + "\'", null );
    }


    public Cursor getCreatedDate(String position) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor CDC = database.rawQuery( "SELECT CREATED_DATE FROM " + TABLE_NAME + " WHERE ID LIKE \'" + position + "\'", null );
        return CDC;
    }

    public Cursor getReminderDate(String position) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor RDC = database.rawQuery( "SELECT REMINDER_DATE FROM " + TABLE_NAME + " WHERE ID LIKE \'" + position + "\'", null );
        return RDC;
    }

    public Cursor getAttachment(String position) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor AC = database.rawQuery( "SELECT * FROM " + IMAGE_TABLE + " WHERE IMAGE_F_KEY LIKE \'" + position + "\'", null );
        return AC;
    }

//    public Cursor getCompletedTask()
//    {
//        SQLiteDatabase database = getWritableDatabase();
//        Cursor CTC = database.rawQuery( " SELECT * FROM "+ TABLE_NAME ,null,null );
//        return CTC;
//    }


    public void deleteOneTask(String position) {
        SQLiteDatabase database = getWritableDatabase();
        int DOT = database.delete( TABLE_NAME, "ID=?", new String[]{String.valueOf( position )} );

    }

    public void deleteSubTasks(String fKey) {
        SQLiteDatabase database = getWritableDatabase();
        int DST = database.delete( SUB_TASK_TABLE, " F_ID=?", new String[]{String.valueOf( fKey )} );
    }

    public void deleteEachSubTask(String pKey) {
        SQLiteDatabase database = getWritableDatabase();
        int DEST = database.delete( SUB_TASK_TABLE, "P_ID=?", new String[]{String.valueOf( pKey )} );
    }

    public void deleteAttachment(String pKey) {
        SQLiteDatabase database = getWritableDatabase();
        int DA = database.delete( IMAGE_TABLE, "IMAGE_P_KEY=?", new String[]{pKey} );
    }

    public void deleteAllAttachments(String fkey) {
        SQLiteDatabase database = getWritableDatabase();
        int DAA = database.delete( IMAGE_TABLE, "IMAGE_F_KEY=?", new String[]{fkey} );
    }

    public void deleteCompletedTasks() {
        SQLiteDatabase database = getWritableDatabase();
        int DCTRs = database.delete( TABLE_NAME, "COMPLETED_TASK=?", new String[]{"yes"} );
    }

    public void deleteEachCompletedTask(String key) {
        SQLiteDatabase database = getWritableDatabase();
        int DCTR = database.delete( TABLE_NAME, "ID=?", new String[]{key} );
    }

    public void deleteSingleRepeatRow(String key) {
        SQLiteDatabase database = getWritableDatabase();
        int DSRR = database.delete( TABLE_NAME, "ID=?", new String[]{key} );
    }

    public boolean isCompleted(String p) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor result = database.rawQuery( "SELECT COMPLETED_TASK FROM " + TABLE_NAME + " WHERE ID LIKE \'" + p + "\'", null );
        boolean isTrue = false;
        if (result.getCount() == 0) {
        }
        while (result.moveToNext()) {
            String isComp = result.getString( 0 );
            if (isComp == null) {
                isTrue = false;
            } else if (isComp.matches( "no" )) {
                isTrue = false;
            } else if (isComp.matches( "yes" )) {
                isTrue = true;
            }
        }
        return isTrue;
    }

    public boolean isRepeated(String p) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor result = database.rawQuery( "SELECT REPETITION FROM " + TABLE_NAME + " WHERE ID LIKE \'" + p + "\'", null );
        boolean isTrue = false;
        if (result.getCount() == 0) {
        }
        while (result.moveToNext()) {
            String isComp = result.getString( 0 );
            if (isComp == null) {
                isTrue = false;
            } else if (isComp.matches( "" )) {
                isTrue = false;
            } else if (isComp.matches( "daily" )) {
                isTrue = true;
            } else if (isComp.matches( "weekly" )) {
                isTrue = true;
            } else if (isComp.matches( "monthly" )) {
                isTrue = true;
            } else if (isComp.matches( "yearly" )) {
                isTrue = true;
            }
        }
        return isTrue;
    }

    public long getIntervalTime(String key) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery( "SELECT INTERVAL_TIME FROM " + TABLE_NAME + " WHERE ID LIKE \'" + key + "\'", null );
        long intervalTime = 0;
        if (cursor.getCount() == 0) {
            //😁
        }
        while (cursor.moveToNext()) {
            String time = cursor.getString( 0 );
            if (time == null) {
            } else if (time != null) {
                intervalTime = Long.parseLong( cursor.getString( 0 ) );
            }
        }
        return intervalTime;
    }


}
