package com.example.reminder.models;

public class AllTasksModel {

    private String taskText;
    private String date;
    private String id;

    public AllTasksModel(String id,String taskText, String date) {
        this.id = id;
        this.taskText = taskText;
        this.date = date;
    }

    public String getId() {return id; }
    public String getTask() {
        return taskText;
    }
    public String getDate() {
        return date;
    }

}
