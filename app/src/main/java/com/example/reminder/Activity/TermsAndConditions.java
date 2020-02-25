package com.example.reminder.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.reminder.R;

public class TermsAndConditions extends AppCompatActivity {

    private Button declineBtn,acceptBtn ;
    private SharedPreferences myPreferences;
    private CheckBox checkBox ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_terms_and_conditions );
        declineBtn = findViewById( R.id.declineBtn );
        acceptBtn = findViewById( R.id.acceptBtn );
        checkBox = findViewById( R.id.tAndCB );
        myPreferences = getApplicationContext().getSharedPreferences( "MY_PREFERENCES", Context.MODE_PRIVATE );
        boolean previouslyStarted = myPreferences.getBoolean("is_Accept", false);
        if(!previouslyStarted) {
            acceptBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked())
                    {
                        SharedPreferences.Editor edit = myPreferences.edit();
                        Intent intent = new Intent( getApplicationContext(),MainActivity.class );
                        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        edit.putBoolean("is_Accept", true);
                        edit.commit();
                        startActivity( intent );
                        finish();
                    }
                    else
                    {

                    }


                }
            } );
            declineBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            } );
        }
        else
            {
                Intent intent = new Intent( getApplicationContext(),MainActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity( intent );
                finish();
            }
            }

}
