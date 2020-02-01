package com.example.reminder.classes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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

        Intent  myIntent = new Intent( this, NotificationReceiver.class );
        myIntent.putExtra( "Task_Title",title );
        myIntent.putExtra( "Position",position );
        PendingIntent  pendingIntent = PendingIntent.getBroadcast( this,position, myIntent, 0 );
        AlarmManager  alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );

        if (alarmManager != null) {
                    alarmManager.cancel( pendingIntent );
                    alarmManager.setExact( AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent );
                }
            }





}
