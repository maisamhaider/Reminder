package com.example.reminder.classes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.reminder.Activity.LauncherActivityOnNotification;
import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {

    private final CharSequence name = "My Channel";// The user-visible name of the channel.
    private NotificationManager mNotificationManager;
    private SharedPreferences myPreferences;
    private NotificationSounds notificationSounds;
    public static final String TAG = "alarm";
    Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mNotificationManager = ((NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE ));
        myPreferences = context.getSharedPreferences( "MY_PREFERENCES", Context.MODE_PRIVATE );
        notificationSounds = new NotificationSounds( context );

        ArrayList<String> soundPaths = notificationSounds.getNotificationSoundsPath();
        String stringSoundPath = soundPaths.get( 0 );
        Uri soundUri = Uri.parse( myPreferences.getString( "NotificationSoundPath", stringSoundPath ) );
        boolean isVibrate = myPreferences.getBoolean( "Is_Vibrate", false );
        String title = intent.getStringExtra( "Task_Title" );
        int position = intent.getIntExtra( "Position", 0 );
        long reminderTime = intent.getLongExtra( "Reminder_Time", 0 );


        if (isVibrate) {
            getVibrateNotification( context, title, position, soundUri, reminderTime );
        } else {
            getNotification( context, title, position, soundUri, reminderTime );

        }

    }

    public void getNotification(Context context, String title, int taskPosition, Uri soundUri , long reminderTime) {
        int notifyID = taskPosition;
        Intent intent = new Intent( context, LauncherActivityOnNotification.class );

        intent.putExtra( "task_title_frm_notification", title );
        intent.putExtra( "task_position_fr_notification", taskPosition );
        intent.putExtra( "Task_Reminder_Time", reminderTime );

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
//                Uri soundUri = Uri.parse( myPreferences.getString( "NotificationSoundPath","" ) );
            NotificationChannel mChannel = new NotificationChannel( CHANNEL_ID, name, importance );
            mChannel.setSound( mChannel.getSound(), null );
            mNotificationManager.createNotificationChannel( mChannel );
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( context, CHANNEL_ID )
                    .setSmallIcon( R.drawable.todolisticon )
                    .setContentTitle( title )
                    .setContentText( "Reminder" )
                    .setOnlyAlertOnce( true )
                    .setSound( soundUri )
                    .setContentIntent( PendingIntent.getActivity( context, notifyID, intent, 0 ) )
                    .setPriority( NotificationCompat.PRIORITY_HIGH )
                    .setAutoCancel( true );
            mNotificationManager.notify( notifyID, mBuilder.build() );
        } else {
//Get an instance of NotificationManager//
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder( context )
                            .setSmallIcon( R.drawable.todolisticon )
                            .setContentTitle( title )
                            .setContentText( "Reminder" )
                            .setOnlyAlertOnce( true )
                            .setSound( soundUri )
                            .setContentIntent( PendingIntent.getActivity( context, notifyID, intent, 0 ) )
                            .setPriority( NotificationCompat.PRIORITY_HIGH )
                            .setAutoCancel( true );
            assert mNotificationManager != null;
            mNotificationManager.notify( notifyID, mBuilder.build() );
        }
    }

    public void getVibrateNotification(Context context, String title, int taskPosition, Uri soundUri , long reminderTime) {
        int notifyID = taskPosition;
        Intent intent = new Intent( context, LauncherActivityOnNotification.class );

        intent.putExtra( "task_title_frm_notification", title );
        intent.putExtra( "task_position_fr_notification", taskPosition );
        intent.putExtra( "Task_Reminder_Time", reminderTime );

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
//                Uri soundUri = Uri.parse( myPreferences.getString( "NotificationSoundPath","" ) );
            NotificationChannel mChannel = new NotificationChannel( CHANNEL_ID, name, importance );
            mChannel.setSound( mChannel.getSound(), null );
            mNotificationManager.createNotificationChannel( mChannel );
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( context, CHANNEL_ID )
                    .setSmallIcon( R.drawable.todolisticon )
                    .setContentTitle( title )
                    .setContentText( "Reminder" )
                    .setOnlyAlertOnce( true )
                    .setSound( soundUri )
                    .setVibrate( new long[]{1000, 1000, 1000, 1000, 1000} )
                    .setContentIntent( PendingIntent.getActivity( context, notifyID, intent, 0 ) )
                    .setPriority( NotificationCompat.PRIORITY_HIGH )
                    .setAutoCancel( true );
            mNotificationManager.notify( notifyID, mBuilder.build() );
        } else {
//Get an instance of NotificationManager//
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder( context )
                            .setContentTitle( title )
                            .setContentText( "Reminder" )
                            .setOnlyAlertOnce( true )
                            .setSound( soundUri )
                            .setSmallIcon( R.drawable.todolisticon )
                            .setVibrate( new long[]{1000, 1000, 1000, 1000, 1000} )
                            .setContentIntent( PendingIntent.getActivity( context, notifyID, intent, 0 ) )
                            .setPriority( NotificationCompat.PRIORITY_HIGH )
                            .setAutoCancel( true );
            assert mNotificationManager != null;
            mNotificationManager.notify( notifyID, mBuilder.build() );
        }
    }

}
