package com.example.reminder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.reminder.Fragments.CalendarFrag;
import com.example.reminder.Fragments.SettingsFrag;
import com.example.reminder.Fragments.AllTasksFrag;
import com.example.reminder.Fragments.mainfrag;
import com.example.reminder.R;
import com.example.reminder.classes.AlarmSettingClass;
import com.example.reminder.classes.NotificationReceiver;
import com.example.reminder.classes.NotificationSounds;
import com.example.reminder.interfaces.EditTextStringListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements EditTextStringListener{


    NotificationReceiver notificationReceiver;
    BottomNavigationView bottomNavigationView;
    EditTextStringListener mEditTextStringListener;
    Fragment mFragment;
    private static final int REQUEST_PERMISSION =1;

    private SharedPreferences myPreferences;
    private NotificationSounds notificationSounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


//        myPreferences = this.getSharedPreferences( "MY_PREFERENCES", Context.MODE_PRIVATE );
//        notificationSounds = new NotificationSounds( this );

//        ArrayList<String> soundPaths = notificationSounds.getNotificationSoundsPath();
//        String stringSoundPath = soundPaths.get( 0 );
//        SharedPreferences.Editor editor = myPreferences.edit();
//          editor.putString( "NotificationSoundPath",stringSoundPath ).commit();


        //Layouts

        //Views
        bottomNavigationView = findViewById( R.id.bottomNV );


        //
        // editTexts

        //buttons

        //list

        //etc

        //function calls
        bottomNavBar();
        setTaskFragDefaultBNBItem();
    }

    public void bottomNavBar()
    {
        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.tasks:
                        AllTasksFrag allTasksFrag = new AllTasksFrag();
                        loadmyfrag( allTasksFrag );
                        break;
                    case R.id.calendar:

                        CalendarFrag calendarFrag = new CalendarFrag();
                        loadmyfrag( calendarFrag );
                        break;
                    case R.id.settings:
                        SettingsFrag settingsFrag = new SettingsFrag();
                        loadmyfrag( settingsFrag );
                        break;

                }

                return true;
            }
        } );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_PERMISSION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Log.i( "Calendar Permission","Granted" );
               setCalendarBNBItem();
            }
            else
            {
                Toast.makeText( this, "Permission is required for working application properly ",
                        Toast.LENGTH_SHORT ).show();
                Log.i( "Calendar Permission","Not Granted" );
            }

        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }




    public void setTaskFragDefaultBNBItem()
    {
        bottomNavigationView.setSelectedItemId( R.id.tasks );

    }
    public void setCalendarBNBItem()
    {
        bottomNavigationView.setSelectedItemId( R.id.calendar );

    }
    public void setSettingsBNBItem()
    {
        bottomNavigationView.setSelectedItemId( R.id.settings );

    }





    public void loadmyfrag(Fragment fragment) {
        this.mFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragcontainer, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {


        MainActivity.this.finish();
        super.onBackPressed();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void hideBottomNView()
    {
        bottomNavigationView.setVisibility( View.GONE );
    }
    public void showBottomNView()
    {
        bottomNavigationView.setVisibility( View.VISIBLE );
    }


    @Override
    public void myString(String ss) {
        if (mFragment instanceof EditTextStringListener)
        mEditTextStringListener = (EditTextStringListener) mFragment;

        if (mEditTextStringListener!=null){
            mEditTextStringListener.myString( ss );
        }
    }

    @Override
    public void myItemPosition(int pos) {

    }


}
