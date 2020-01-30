package com.example.reminder.classes;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;

import androidx.core.app.NotificationCompat;

import com.example.reminder.Activity.LauncherActivityOnNotification;
import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;

import java.util.Calendar;
import java.util.Date;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class AlarmReceiver extends ContextWrapper {

    private static final String ACTION_SNOOZE = "SNOOZE" ;
    final CharSequence name = "My Channel";// The user-visible name of the channel.
    NotificationManager mNotificationManager;
    final String TAG="alarm";
    PendingIntent pendingIntent;
    PendingIntent snoozePendingIntent;



    public AlarmReceiver(Context base) {
        super( base );



        DataBaseHelper dataBaseHelper;
        MyTimeSettingClass myTimeSettingClass;
        String taskPosition, taskTitle;
        long reminderTime;


        Intent myIntent;
        Intent snoozeIntent;
        AlarmManager alarmManager;
        String isAlarm;


        myIntent = new Intent( this, NotificationReceiver.class );
        snoozeIntent = new Intent( this,NotificationReceiver.class  );
        alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );
            snoozeIntent.setAction( ACTION_SNOOZE );
        dataBaseHelper = new DataBaseHelper( this );
        myTimeSettingClass = new MyTimeSettingClass();


        Cursor cursor = dataBaseHelper.getAllTasks();
        if (cursor.getCount() == 0) {
        }
        while (cursor.moveToNext()) {
            isAlarm = cursor.getString( 8 );
            taskPosition = cursor.getString( 0 );
            taskTitle = cursor.getString( 1 );
            reminderTime = myTimeSettingClass.getMilliFromDate( cursor.getString( 2 ) );


            if (isAlarm.matches( "1" )) {
                myIntent.putExtra( "Title", taskTitle );
                myIntent.putExtra( "Position", taskPosition );
                snoozeIntent.putExtra(taskPosition, 0);

                pendingIntent = PendingIntent.getBroadcast( this, 0, myIntent, 0 );

                if (alarmManager!=null)
                {
                    alarmManager.setInexactRepeating( AlarmManager.RTC_WAKEUP, reminderTime, AlarmManager.INTERVAL_DAY, pendingIntent );


                }
                else
                    alarmManager.cancel( pendingIntent );


            }


        }


//        Toast.makeText(getApplicationContext(), "App reminder is On", Toast.LENGTH_SHORT).show();


//        calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            calendar.set(Calendar.MINUTE, minut);
//            calendar.set(Calendar.HOUR, hr);
//            calendar.set(Calendar.SECOND, 0);
//            if (AM_PM.equals("AM"))
//                calendar.set( AM_PM,Calendar.AM );
//            else
//                calendar.set( AM_PM,Calendar.AM );
//            if (isAlarmOn) {
//        if (alarmManager != null)
//            alarmManager.cancel(pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        } else {
//            alarmManager.cancel(pendingIntent);


    }

    public void getNotification(Context context, String title,String taskPosition) {
        int notifyID = Integer.parseInt( taskPosition );
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
//                Uri soundUri = Uri.parse( myPreferences.getString( "NotificationSoundPath","" ) );

            NotificationChannel mChannel = new NotificationChannel( CHANNEL_ID, name, importance);
            mChannel.setSound( mChannel.getSound(), null);
            mNotificationManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
            mNotificationManager.createNotificationChannel(mChannel);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon( R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText("Reminder")
                    .setOnlyAlertOnce(true)
                    .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context,
                            LauncherActivityOnNotification.class), 0))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(false).addAction( R.drawable.ic_launcher_foreground,"snooze",snoozePendingIntent );
            mNotificationManager.notify(notifyID, mBuilder.build());
        } else {
//Get an instance of NotificationManager//
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText("Reminder")
                            .setOnlyAlertOnce(true)
                            .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context,
                                    LauncherActivityOnNotification.class), 0))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .addAction(R.drawable.ic_launcher_foreground, "snooze", snoozePendingIntent)
                            .setAutoCancel(false);
            // Gets an instance of the NotificationManager service//
            assert mNotificationManager != null;
            mNotificationManager.notify(notifyID, mBuilder.build());
        }
    }


}
