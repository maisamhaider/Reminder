package com.example.reminder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.reminder.R;
import com.example.reminder.adapter.CompletedTaskAdapter;
import com.example.reminder.utilities.AlarmSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.models.CompletedTasksModel;

import java.util.ArrayList;

public class CompletedTasksActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    RecyclerView recyclerView;
    CompletedTaskAdapter completedTaskAdapter;

    String checkCompleted = "";
    String title;
    String date = "";
    String id;

    LinearLayout completedTaskDeleteLL,CompletedTaskBackLL;
    private AlarmSettingClass alarmSettingClass;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_completed_tasks );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run activity only in portrait mode

        alarmSettingClass = new AlarmSettingClass( this );

        dataBaseHelper = new DataBaseHelper( this );
        recyclerView = findViewById( R.id.completedTaskRecyclerView );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );


        completedTaskDeleteLL = findViewById( R.id.completedTaskDeleteLL );
        CompletedTaskBackLL = findViewById( R.id.CompletedTaskBackLL );
        dataFromDb();
        CompletedTaskBackLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        } );

        completedTaskDeleteLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {


                Cursor cursor = dataBaseHelper.getAllTasks();
                if (cursor.getCount()==0)
                {}
                while (cursor.moveToNext())
                {
                    String position = cursor.getString( 0 );
                    String isCompleted = cursor.getString( 6 );
                    if (isCompleted==null)
                    {}
                    else
                    if (isCompleted.matches( "yes" ))
                    {
                        alarmSettingClass.deleteRepeatAlarm( Integer.parseInt( position ) );
                        dataBaseHelper.deleteEachCompletedTask(position);
                    }
                    else
                    {}

                }
                dataFromDb();
               }
               catch (Exception e)
               {
                   //error
               }
            }
        } );


    }
    public void dataFromDb()
    {
        ArrayList<CompletedTasksModel>list =new ArrayList<>(  );
        Cursor cursor = dataBaseHelper.getAllTasks();
        if (cursor.getCount()==0)
        {
        }
        while (cursor.moveToNext())
        {
            checkCompleted = cursor.getString( 6 );
            title = cursor.getString( 1 );
            date  = cursor.getString( 2 );
            id = cursor.getString( 0 );


            if (date.matches( "" ))
            {
                date = "";
            }
            if (checkCompleted==null)
            {
                //😁😁😁😁😁😁😁 i am free. no work
            }
            else
            if (checkCompleted.matches( "yes" ))
            {
                //😢 adding completed task to list.to much work is here 😣
                list.add( new CompletedTasksModel( title,date,id ) );

            }
            else
            {
            // yahoo.nothing to do now 😂🤣😅😁😀
            }
        }
        completedTaskAdapter = new CompletedTaskAdapter( this,list,dataBaseHelper );
        recyclerView.setAdapter( completedTaskAdapter );
        completedTaskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
