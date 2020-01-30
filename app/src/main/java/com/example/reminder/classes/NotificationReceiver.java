package com.example.reminder.classes;

import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.reminder.Activity.LauncherActivityOnNotification;
import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;

import java.util.Date;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class NotificationReceiver extends BroadcastReceiver {

        private final CharSequence name = "My Channel";// The user-visible name of the channel.
        private NotificationManager mNotificationManager;
        public static final String TAG="alarm";
        Context mContext;
        private  AlarmReceiver alarmReceiver;


//    SharedPreferences  myPreferences = mContext.getSharedPreferences( "MY_PREFERENCES", Context.MODE_PRIVATE );

    @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            String title = intent.getStringExtra( "Title" );
            String  position = intent.getStringExtra( "Position");
            alarmReceiver = new AlarmReceiver(context );
            alarmReceiver.getNotification( mContext,title,position );
            if (intent.getAction().matches( "snooze" ))
            {
                alarmReceiver.getNotification( mContext,title,position );
            }

            Toast.makeText( context, "broadcast", Toast.LENGTH_SHORT ).show();
            Log.i( TAG, "onReceive: Alaram" );


        }

//
//        private void getNotification(String title,String taskPosition) {
//            int notifyID = Integer.parseInt( taskPosition );
//
//
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                String CHANNEL_ID = "my_channel_01";// The id of the channel.
//                int importance = NotificationManager.IMPORTANCE_HIGH;
////                Uri soundUri = Uri.parse( myPreferences.getString( "NotificationSoundPath","" ) );
//
//                NotificationChannel mChannel = new NotificationChannel( CHANNEL_ID, name, importance);
//                mChannel.setSound( mChannel.getSound(), null);
//
//                assert mNotificationManager != null;
//                mNotificationManager.createNotificationChannel(mChannel);
//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(title)
//                        .setContentText("Reminder")
//                        .setOnlyAlertOnce(true)
//                        .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(mContext,
//                                LauncherActivityOnNotification.class), 0))
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setAutoCancel(false);
//                mNotificationManager.notify(notifyID, mBuilder.build());
//            } else {
////Get an instance of NotificationManager//
//                NotificationCompat.Builder mBuilder =
//                        new NotificationCompat.Builder(mContext)
//                                .setSmallIcon(R.mipmap.ic_launcher)
//                                .setContentTitle(title)
//                                .setContentText("Reminder")
//                                .setOnlyAlertOnce(true)
//                                .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(mContext,
//                                        LauncherActivityOnNotification.class), 0))
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
////                                .addAction(R.drawable.ic_launcher_foreground, mContext.getString(R.string.app_name), snoozePendingIntent)
//                                .setAutoCancel(false);
//                // Gets an instance of the NotificationManager service//
//                assert mNotificationManager != null;
//                mNotificationManager.notify(notifyID, mBuilder.build());
//            }
//        }
}
