package com.example.reminder.classes;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import com.example.reminder.database.DataBaseHelper;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {

        private DataBaseHelper dataBaseHelper;
        private final CharSequence name = "My Channel";// The user-visible name of the channel.
        private NotificationManager mNotificationManager;
        MyTimeSettingClass myTimeSettingClass;
        public static final String TAG="alarm";
        Context mContext;
        private AlarmSettingClass alarmSettingClass;


//    SharedPreferences  myPreferences = mContext.getSharedPreferences( "MY_PREFERENCES", Context.MODE_PRIVATE );

    @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            myTimeSettingClass = new MyTimeSettingClass();
            dataBaseHelper = new DataBaseHelper( context );
            String title = intent.getStringExtra( "Title" );
            String  position = intent.getStringExtra( "Position");
//            Calendar calendar = Calendar.getInstance();
//            String currentTimeString = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" ).format( calendar.getTime() );
//            long snoozedTime = myTimeSettingClass.getMilliFromDate( currentTimeString )+ 5*60000;
            alarmSettingClass = new AlarmSettingClass(context );
            alarmSettingClass.getNotification( context,title,position );
//            String action = intent.getAction();
//
//            if (action!=null && action.equals( "SNOOZE" ))
//            {
//
//               String formattedTimeString = MyTimeSettingClass.getFormattedDateFromMilliseconds( snoozedTime );
//               boolean isUpdated = dataBaseHelper.updateReminderTime( position,formattedTimeString );
//               if (isUpdated)
//               {
//                   alarmSettingClass.setAllAlarm();
//                   Toast.makeText( context, "update", Toast.LENGTH_SHORT ).show();
//               }
//               else
//                   {
//                       Toast.makeText( context, "not update", Toast.LENGTH_SHORT ).show();
//                   }
//
//
//            }
//            else
//            if (action!=null && action.equals( "DONE" ))
//            {
//                dataBaseHelper.upDate(position,"yes","0");
//                alarmSettingClass.setAllAlarm();
//
//
//            }
//            else
//            {
//                dataBaseHelper.upDate( position,"yes","0" );
//                alarmSettingClass.setAllAlarm();
//
//            }

            Toast.makeText( context, "broadcast", Toast.LENGTH_SHORT ).show();
            Log.i( TAG, "onReceive: Alarm" );


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
