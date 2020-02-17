package com.example.reminder.classes;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.reminder.database.DataBaseHelper;

public class DeviceBootReceiver extends BroadcastReceiver {

    private AlarmSettingClass alarmSettingClass;
    private DataBaseHelper dataBaseHelper;
    
    @Override
    public void onReceive(Context context, Intent intent) {

        dataBaseHelper = new DataBaseHelper( context );
        alarmSettingClass = new AlarmSettingClass( context );
        if (intent.getAction().equals( "android.intent.action.BOOT_COMPLETED" ))
        {
            Cursor cursor = dataBaseHelper.getAllTasks();
            if (cursor.getCount()==0)
            {}
            while (cursor.moveToNext())
            {
                String taskPosition = cursor.getString( 0 );
                String taskTitle = cursor.getString( 1 );
                String reminderTime = cursor.getString( 2 );
                String isCompleted = cursor.getString( 6 );
                String isRepeat = cursor.getString( 7);
                String isAlarm = cursor.getString( 8 );
                long reminderMilliSecond = MyTimeSettingClass.getMilliFromDate( reminderTime );
                int taskPositionInt = Integer.parseInt( taskPosition );

                if (!isRepeat.matches( "" ))
                {
                    if (isRepeat.matches( "daily" ))
                    {

                        alarmSettingClass.setRepeatingAlarm(taskTitle,reminderMilliSecond,taskPositionInt, AlarmManager.INTERVAL_DAY );
                    }else if (isRepeat.matches( "weekly" ))
                    {
                        alarmSettingClass.setRepeatingAlarm(taskTitle,reminderMilliSecond,taskPositionInt, AlarmManager.INTERVAL_DAY * 7);
                    }
                    else if (isRepeat.matches( "monthly" ))
                    {
                        alarmSettingClass.setRepeatingAlarm(taskTitle,reminderMilliSecond,taskPositionInt, AlarmManager.INTERVAL_DAY * 30);
                    }
                    else if (isRepeat.matches( "yearly" ))
                    {
                        alarmSettingClass.setRepeatingAlarm(taskTitle,reminderMilliSecond,taskPositionInt, AlarmManager.INTERVAL_DAY * 365);
                    }

                }
                else
                if (isAlarm.matches( "1" ) && isRepeat.matches( "" ) )
                {
                    alarmSettingClass.setOneAlarm( taskTitle,reminderMilliSecond,taskPositionInt);
                }

            }
        }

    }
}
