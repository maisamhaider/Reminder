package com.example.reminder.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reminder.R;
import com.example.reminder.utilities.AlarmSettingClass;
import com.example.reminder.utilities.MyTimeSettingClass;
import com.example.reminder.utilities.NotificationReceiver;
import com.example.reminder.database.DataBaseHelper;

import java.util.Calendar;

public class LauncherActivityOnNotification extends AppCompatActivity {

    ImageView snoozeIv, doneIv;
    TextView whatToDoWithTv;
    DataBaseHelper dataBaseHelper;

    AlarmSettingClass alarmSettingClass;
    private MyTimeSettingClass myTimeSettingClass;
    private MainActivity mainActivity;
    int position;
    String taskTitle;
    boolean isRepeating;
    long snoozedTime, intervalTime;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_launcher_on_notification );
        mainActivity = new MainActivity();

        myTimeSettingClass = new MyTimeSettingClass();
        dataBaseHelper = new DataBaseHelper( this );
        snoozeIv = findViewById( R.id.alarmSnoozeIv );
        doneIv = findViewById( R.id.alarmDoneIv );
        whatToDoWithTv = findViewById( R.id.whatToDoWithTv );
        alarmSettingClass = new AlarmSettingClass( this );


        Calendar calendar = Calendar.getInstance();
        @SuppressLint({"NewApi", "LocalSuppress"}) String currentTimeString = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" ).format( calendar.getTime() );
        snoozedTime = myTimeSettingClass.getMilliFromDate( currentTimeString ) + 5 * 60000;
        Intent intent = getIntent();


        position = intent.getIntExtra( "task_position_fr_notification", 0 );
        taskTitle = intent.getStringExtra( "task_title_frm_notification" );

        whatToDoWithTv.setText( "What to do with \"" + taskTitle + "\" ?" );

        snoozeIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent snoozeIntent = new Intent( getApplicationContext(), NotificationReceiver.class );
                snoozeIntent.putExtra( "Title", taskTitle );
                snoozeIntent.putExtra( "Position", taskTitle );
                PendingIntent snoozePendingIntent = PendingIntent.getBroadcast( getApplicationContext(), position, snoozeIntent, 0 );
                AlarmManager alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );

                if (alarmManager != null) {
                    alarmManager.cancel( snoozePendingIntent );
                    alarmManager.setExact( AlarmManager.RTC_WAKEUP, snoozedTime, snoozePendingIntent );
                } else {
                    alarmManager.cancel( snoozePendingIntent );
                }

                finish();
            }


        } );


        doneIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isRepeating =   dataBaseHelper.isRepeated( String.valueOf( position ) );
               long iT = dataBaseHelper.getIntervalTime( String.valueOf( position ) );
                if (iT==0)
                {
                    //nothing then 😉
                }
                else
                    { intervalTime = iT;
                    }
                if (!isRepeating) {
                    dataBaseHelper.upDate( String.valueOf( position ), "yes", "0" );
                    finish();
                } else {
//                    dataBaseHelper.update( MyTimeSettingClass.getFormattedDateFromMilliseconds( reminderTime ), String.valueOf( position ) );
//                    dataBaseHelper.upDate( String.valueOf( position ), "no", "1" );
                    updateDateWhenTriggered(taskTitle, String.valueOf( position ),intervalTime);
                    finish();


                }

            }
        } );

    }

    public boolean updateDateWhenTriggered(String title,String id, long intervalTime) {
                int pos = Integer.parseInt( id );
        if (intervalTime == AlarmManager.INTERVAL_DAY) {
            // daily
            String d = MyTimeSettingClass.getIntervalTime( 1 );
            dataBaseHelper.update( MyTimeSettingClass.getIntervalTime( 1 ), id );
            dataBaseHelper.upDate( id, "no", "1" );
            alarmSettingClass.setRepeatingAlarm( title,MyTimeSettingClass.getMilliFromDate( MyTimeSettingClass.getIntervalTime( 1 ) )
                    ,pos,intervalTime );

        } else if (intervalTime == AlarmManager.INTERVAL_DAY * 7) {
            // weekly
            dataBaseHelper.update( MyTimeSettingClass.getIntervalTime( 7 ),id );
            dataBaseHelper.upDate( id, "no", "1" );
            alarmSettingClass.setRepeatingAlarm( title,MyTimeSettingClass.getMilliFromDate( MyTimeSettingClass.getIntervalTime( 7 ) )
                    ,pos,intervalTime );
        } else if (intervalTime == AlarmManager.INTERVAL_DAY * 30) {
            //monthly
            dataBaseHelper.update( MyTimeSettingClass.getIntervalTime( 30),id );
            dataBaseHelper.upDate( id, "no", "1" );
            alarmSettingClass.setRepeatingAlarm( title,MyTimeSettingClass.getMilliFromDate( MyTimeSettingClass.getIntervalTime( 30 ) )
                    ,pos,intervalTime );

        } else if (intervalTime == AlarmManager.INTERVAL_DAY * 365) {
            //yearly
            dataBaseHelper.update( MyTimeSettingClass.getIntervalTime( 365 ),id);
            dataBaseHelper.upDate( id, "no", "1" );
            alarmSettingClass.setRepeatingAlarm( title,MyTimeSettingClass.getMilliFromDate( MyTimeSettingClass.getIntervalTime( 356 ) )
                    ,pos,intervalTime );
        }


        return true;
    }
}
