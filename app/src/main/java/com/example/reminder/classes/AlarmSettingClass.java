package com.example.reminder.classes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;

import androidx.core.app.NotificationCompat;

import com.example.reminder.Activity.LauncherActivityOnNotification;
import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;

import java.util.Calendar;
import java.util.Date;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class AlarmSettingClass extends ContextWrapper {


    public AlarmSettingClass(Context base) {
        super( base );


    }
    public void setOneAlarm(String title,long reminderTime,int position)
    {
        AlarmManager  alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );
        Intent  myIntent = new Intent( this, NotificationReceiver.class );
        myIntent.putExtra( "Task_Title",title );
        myIntent.putExtra( "Position",position );

        PendingIntent  pendingIntent = PendingIntent.getBroadcast( this,position, myIntent, 0 );

        if (alarmManager != null) {
                    alarmManager.cancel( pendingIntent );
                    alarmManager.setExact( AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent );
                }
            }
//    public void alarmEditFun(String title,long reminderTime,int position)
//    {
//        AlarmManager  alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );
//        Intent  myIntent = new Intent( this, NotificationReceiver.class );
//        myIntent.putExtra( "Task_Title",title );
//        myIntent.putExtra( "Position",position );
//        myIntent.putExtra( "Is_Repeating",false );
//
//        PendingIntent  pendingIntent = PendingIntent.getBroadcast( this,position, myIntent, PendingIntent.FLAG_UPDATE_CURRENT );
//
//        if (alarmManager != null) {
//            alarmManager.cancel( pendingIntent );
//            alarmManager.setExact( AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent );
//        }
//    }

    public void setRepeatingAlarm(String title,long reminderTime,int position,long intervalTime )
    {


        Intent  myIntent1 = new Intent( this, NotificationReceiver.class );
        myIntent1.putExtra( "Task_Title",title );
        myIntent1.putExtra( "Is_Repeating",true );
        myIntent1.putExtra( "Position",position );
        myIntent1.putExtra( "Reminder_Time", reminderTime);


        PendingIntent  pendingIntent1 = PendingIntent.getBroadcast( this,position, myIntent1, 0 );
        AlarmManager  alarmManager1 = (AlarmManager) getSystemService( ALARM_SERVICE );

        if (alarmManager1 != null) {
            alarmManager1.cancel( pendingIntent1 );
            alarmManager1.setRepeating( AlarmManager.RTC_WAKEUP, reminderTime,intervalTime , pendingIntent1 );
        }
    }


    public void deleteRepeatAlarm(int position)
    {
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), position , intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }



}
