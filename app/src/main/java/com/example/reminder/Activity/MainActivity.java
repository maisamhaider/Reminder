package com.example.reminder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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
        setDefaultBNBItem();

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

    public void setDefaultBNBItem()
    {
        bottomNavigationView.setSelectedItemId( R.id.tasks );

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
        System.exit(0);
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
    public void mystring(String ss) {
        if (mFragment instanceof EditTextStringListener)
        mEditTextStringListener = (EditTextStringListener) mFragment;

        if (mEditTextStringListener!=null){
            mEditTextStringListener.mystring( ss );
        }
    }
}
