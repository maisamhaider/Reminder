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

    private static final String ACTION_SNOOZE = "SNOOZE" ;
    private static final String ACTION_DONE = "DONE" ;
    final CharSequence name = "My Channel";// The user-visible name of the channel.
    NotificationManager mNotificationManager;
    final String TAG="alarm";


    Intent snoozeIntent;
    Intent myIntent;
    Intent doneIntent;

    PendingIntent pendingIntent;

    AlarmManager alarmManager;

    long reminderTime;

    DataBaseHelper dataBaseHelper;
    MyTimeSettingClass myTimeSettingClass;
    String taskPosition, taskTitle;


    String isAlarm;


    public AlarmSettingClass(Context base) {
        super( base );



        myIntent = new Intent( this, NotificationReceiver.class );
        alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );
        dataBaseHelper = new DataBaseHelper( this );
        myTimeSettingClass = new MyTimeSettingClass();


    }


    public void setAllAlarm()
    {

    Cursor cursor = dataBaseHelper.getAllTasks();
        if (cursor.getCount() == 0) {
    }
        while (cursor.moveToNext()) {
            isAlarm = cursor.getString( 8 );
            taskPosition = cursor.getString( 0 );
            taskTitle = cursor.getString( 1 );
            reminderTime = myTimeSettingClass.getMilliFromDate( cursor.getString( 2 ) );

            Calendar calendar = Calendar.getInstance();
            String stringCurrentTime = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" ).format( calendar.getTime() );
            long longCurrentTime = myTimeSettingClass.getMilliFromDate( stringCurrentTime );
            ;

            if ((longCurrentTime <=reminderTime) && isAlarm.matches( "1" )) {
                myIntent.putExtra( "Title", taskTitle );
                myIntent.putExtra( "Position", taskPosition );
                myIntent.putExtra( "reminder_time", reminderTime );

                pendingIntent = PendingIntent.getBroadcast( this, 0, myIntent, 0 );

                if (alarmManager != null) {
                    alarmManager.cancel( pendingIntent );
                    alarmManager.setInexactRepeating( AlarmManager.RTC_WAKEUP, reminderTime, AlarmManager.INTERVAL_DAY, pendingIntent );
                } else
                    alarmManager.cancel( pendingIntent );
            }

        }
    }

    public void getNotification(Context context, String title,String taskPosition) {
        int notifyID = Integer.parseInt( taskPosition );
           Intent intent = new Intent(context, LauncherActivityOnNotification.class);

           intent.putExtra( "task_title_frm_notification",title );
           intent.putExtra( "task_position_fr_notification",taskPosition );

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
                    .setContentIntent(PendingIntent.getActivity(context, 0,intent, 0))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel( true );
            mNotificationManager.notify(notifyID, mBuilder.build());
        } else {
//Get an instance of NotificationManager//
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText("Reminder")
                            .setOnlyAlertOnce(true)
                            .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true);
            // Gets an instance of the NotificationManager service//
            assert mNotificationManager != null;
            mNotificationManager.notify(notifyID, mBuilder.build());
        }
    }


}
