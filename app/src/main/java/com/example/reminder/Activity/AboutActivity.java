package com.example.reminder.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reminder.R;

public class AboutActivity extends AppCompatActivity {


    private  TextView applicationNameTV,applicationVersion;
    private ImageView aboutBackIv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about );

        applicationNameTV = findViewById( R.id.applicationNameTV );
        applicationVersion = findViewById( R.id.applicationVersion );
        aboutBackIv = findViewById( R.id.aboutBackIv );
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            applicationVersion.setText("Version "+ version );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        aboutBackIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

    }


}
