package com.example.reminder.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder.R;
import com.example.reminder.classes.AlarmSettingClass;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.classes.NotificationReceiver;
import com.example.reminder.database.DataBaseHelper;

import java.util.Calendar;

public class LauncherActivityOnNotification extends AppCompatActivity {

    Button snoozeButton, doneButton;
    TextView whatToDoWithTv;
    DataBaseHelper dataBaseHelper;

    AlarmSettingClass alarmSettingClass;
    private MyTimeSettingClass myTimeSettingClass;
     private MainActivity mainActivity;
     int position;
     String taskTitle;
     long snoozedTime;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_launcher_on_notification );
        mainActivity = new MainActivity();

        myTimeSettingClass = new MyTimeSettingClass();
        dataBaseHelper = new DataBaseHelper( this );
        snoozeButton = findViewById( R.id.alarmSnoozeBtn );
        doneButton = findViewById( R.id.alarmDoneBtn );
        whatToDoWithTv = findViewById( R.id.whatToDoWithTv );
        alarmSettingClass = new AlarmSettingClass( this );


        Calendar calendar = Calendar.getInstance();
        String currentTimeString = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" ).format( calendar.getTime() );
        snoozedTime = myTimeSettingClass.getMilliFromDate( currentTimeString ) + 5 * 60000;
        Intent intent = getIntent();


        position = intent.getIntExtra( "task_position_fr_notification",0 );
        taskTitle = intent.getStringExtra("task_title_frm_notification");
        whatToDoWithTv.setText( "What to with "+taskTitle+"?" );

        snoozeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent snoozeIntent = new Intent(getApplicationContext(), NotificationReceiver.class );
                snoozeIntent.putExtra( "Title",taskTitle );
                snoozeIntent.putExtra( "Position",taskTitle );
                PendingIntent snoozePendingIntent = PendingIntent.getBroadcast( getApplicationContext(),position,snoozeIntent,0 ) ;
                AlarmManager alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );

                if (alarmManager != null) {
                    alarmManager.cancel( snoozePendingIntent );
                    alarmManager.setExact( AlarmManager.RTC_WAKEUP, snoozedTime, snoozePendingIntent );
                } else
                {
                    alarmManager.cancel( snoozePendingIntent );

                }
                finish();
            }


        } );

        doneButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.upDate( String.valueOf( position ),"yes","0");
                finish();
            }
        } );


    }
}
