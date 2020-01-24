package com.example.reminder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.reminder.R;
import com.example.reminder.adapter.CompletedTaskAdapter;
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

    LinearLayout completedTaskDeleteLL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_completed_tasks );


        dataBaseHelper = new DataBaseHelper( this );
        recyclerView = findViewById( R.id.completedTaskRecyclerView );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );

        completedTaskDeleteLL = findViewById( R.id.completedTaskDeleteLL );

        dataFromDb();

        completedTaskDeleteLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.deleteCompletedTasks();
                dataFromDb();
            }
        } );


    }
    public void dataFromDb()
    {
        ArrayList<CompletedTasksModel>list =new ArrayList<>(  );
        Cursor cursor = dataBaseHelper.getCompletedTask();
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

            }
            else
            if (checkCompleted.matches( "yes" ))
            {
                list.add( new CompletedTasksModel( title,date,id ) );
            }
            else
            {

            }
        }
        completedTaskAdapter = new CompletedTaskAdapter( this,list,dataBaseHelper );
        recyclerView.setAdapter( completedTaskAdapter );
        completedTaskAdapter.notifyDataSetChanged();
    }


}
