package com.example.reminder.classes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.reminder.R;

public class NotificationHelper extends ContextWrapper {

    private static final String channel_id = "1";
    private static final String channel_name = "TASK_ALARM";

    private NotificationManager notificationManager;

   SharedPreferences myPreferences = this.getSharedPreferences( "MY_PREFERENCES", Context.MODE_PRIVATE );



    public NotificationHelper(Context base) {
        super( base );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel();
    }

    public void createChannel()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel( channel_id,channel_name, NotificationManager.IMPORTANCE_HIGH );
            getNotificationManager().createNotificationChannel(channel);
        }

    }

    public NotificationManager getNotificationManager()
    {
        if (notificationManager==null)
        {
            notificationManager = (NotificationManager)getSystemService( Context.NOTIFICATION_SERVICE );

        }

        return notificationManager;
    }

    public NotificationCompat.Builder getChannelNotification()
    {
        return new NotificationCompat.Builder( getApplicationContext(),channel_id )
                .setContentTitle( "Title" )
                .setContentText( "Reminder" )
                .setSmallIcon( android.R.drawable.ic_notification_overlay )
                .setPriority( NotificationCompat.PRIORITY_DEFAULT )
                .setSound( Uri.parse(myPreferences.getString( "NotificationSoundPath","")) );
    }


}
