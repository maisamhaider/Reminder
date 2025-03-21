package com.example.reminder.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.reminder.R;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );

        handler = new Handler( );
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent( SplashActivity.this,TermsAndConditions.class );
                startActivity( intent );
                finish();
            }
        },2000);
    }
}
