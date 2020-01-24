package com.example.reminder.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.reminder.R;

public class AboutActivity extends AppCompatActivity {


    private  TextView applicationNameTV,applicationVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about );

        applicationNameTV = findViewById( R.id.applicationNameTV );
        applicationVersion = findViewById( R.id.applicationVersion );
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            applicationVersion.setText( version );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


}
