package com.example.reminder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.reminder.Fragments.CalendarFrag;
import com.example.reminder.Fragments.SettingsFrag;
import com.example.reminder.Fragments.AllTasksFrag;
import com.example.reminder.R;
import com.example.reminder.interfaces.EditTextStringListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements EditTextStringListener{


    ScrollView scrollView;
    LinearLayout bottomsheetLLO;
    BottomNavigationView bottomNavigationView;
    EditTextStringListener mEditTextStringListener;
    Fragment mFragment;
    private static final int REQUEST_PERMISSION =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


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



}
