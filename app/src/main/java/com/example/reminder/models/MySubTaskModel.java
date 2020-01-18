package com.example.reminder.models;

public class MySubTaskModel {

    String subTaskTitle;
    String id;

    public MySubTaskModel(String id,String subTaskTitle) {
        this.subTaskTitle = subTaskTitle;
        this.id = id;
    }

    public String getSubTaskTitle() {
        return subTaskTitle;
    }

    public String getId() {
        return id;
    }
}
