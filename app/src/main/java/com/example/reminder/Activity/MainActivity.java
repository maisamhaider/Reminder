package com.example.reminder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder.Fragments.CalendarFrag;
import com.example.reminder.Fragments.SettingsFrag;
import com.example.reminder.Fragments.TasksFrag;
import com.example.reminder.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class MainActivity extends AppCompatActivity {


    ScrollView scrollView;
    LinearLayout bottomsheetLLO;
    BottomNavigationView bottomNavigationView;

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

        TasksFrag tasksFrag = new TasksFrag();
        loadmyfrag( tasksFrag );

    }

    public void bottomNavBar()
    {
        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.tasks:
                        TasksFrag tasksFrag = new TasksFrag();
                        loadmyfrag( tasksFrag );
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




    public void loadmyfrag(Fragment fragment) {
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


    public void hideBottonNView()
    {
        bottomNavigationView.setVisibility( View.GONE );
    }
    public void showBottonNView()
    {
        bottomNavigationView.setVisibility( View.VISIBLE );
    }




}
