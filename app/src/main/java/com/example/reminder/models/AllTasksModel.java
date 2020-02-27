package com.example.reminder.models;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;

public class AllTasksModel {

    private String taskText;
    private String date;
    private String id;
    private boolean isCompleted;

    public AllTasksModel() {
    }


    public AllTasksModel(String id,String taskText, String date,boolean isCompleted) {
        this.id = id;
        this.taskText = taskText;
        this.date = date;
        this.isCompleted = isCompleted;
    }

    public String getId() {return id; }
    public String getTask() {
        return taskText;
    }
    public String getDate() {
        return date;
    }
    public boolean isCompleted() { return isCompleted;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public void setDate(String date) {
        this.date = date;
    }
    @SuppressLint("NewApi")
    public Date getMDate() {
        @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat format = new SimpleDateFormat(
                "dd MMM yyyy EEE, h:mm a"    );
        Date date = new Date(  );
        try {
            date = format.parse( getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date ;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }


}
