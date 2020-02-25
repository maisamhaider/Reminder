package com.example.reminder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.reminder.Fragments.CalendarFrag;
import com.example.reminder.Fragments.SettingsFrag;
import com.example.reminder.Fragments.AllTasksFrag;
import com.example.reminder.Fragments.mainfrag;
import com.example.reminder.R;
import com.example.reminder.classes.NotificationReceiver;
import com.example.reminder.classes.NotificationSounds;
import com.example.reminder.interfaces.EditTextStringListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements EditTextStringListener {


    NotificationReceiver notificationReceiver;
    BottomNavigationView bottomNavigationView;
    EditTextStringListener mEditTextStringListener;
    Fragment mFragment;
    private static final int REQUEST_PERMISSION = 1;

    private SharedPreferences myPreferences;
    private NotificationSounds notificationSounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        checkPermission();
        mainfrag mf = new mainfrag();
        loadmyfrag( mf );



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

    public void bottomNavBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
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

    // read/write and camera permissions
    public boolean checkPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission( this, Manifest.permission.CAMERA );
        int readStoragePermission = ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE );
        int writeStoragePermission = ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE );
        int calendarWritePermission = ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_CALENDAR );
        int calendarReaDPermission = ContextCompat.checkSelfPermission( this, Manifest.permission.READ_CALENDAR );
        int audioPermission = ContextCompat.checkSelfPermission( this, Manifest.permission.RECORD_AUDIO );

        if (cameraPermission == PackageManager.PERMISSION_GRANTED && readStoragePermission == PackageManager.PERMISSION_GRANTED
                && writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && calendarWritePermission == PackageManager.PERMISSION_GRANTED
                && calendarReaDPermission == PackageManager.PERMISSION_GRANTED
                && audioPermission == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        } else {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.RECORD_AUDIO
            }, REQUEST_PERMISSION );
            return false;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i( "Calendar Permission", "Granted" );
            } else {
                Toast.makeText( this, "Permission is required for working application properly ",
                        Toast.LENGTH_SHORT ).show();
                Log.i( "Calendar Permission", "Not Granted" );
            }

        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }


    public void setTaskFragDefaultBNBItem() {

        bottomNavigationView.setSelectedItemId( R.id.tasks );

    }

    public void setCalendarBNBItem() {
        bottomNavigationView.setSelectedItemId( R.id.calendar );

    }

    public void setSettingsBNBItem() {
        bottomNavigationView.setSelectedItemId( R.id.settings );

    }


    public void loadmyfrag(Fragment fragment) {
        this.mFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace( R.id.fragcontainer, fragment );
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            showBottomNView();
            getSupportFragmentManager().popBackStack();
        } else {
            MainActivity.this.finish();
            super.onBackPressed();

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void hideBottomNView() {
        bottomNavigationView.setVisibility( View.GONE );
    }

    public void showBottomNView() {
        bottomNavigationView.setVisibility( View.VISIBLE );
    }


    @Override
    public void myString(String ss) {
        if (mFragment instanceof EditTextStringListener)
            mEditTextStringListener = (EditTextStringListener) mFragment;

        if (mEditTextStringListener != null) {
            mEditTextStringListener.myString( ss );
        }
    }

    @Override
    public void myItemPosition(int pos) {

    }


}
