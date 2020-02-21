package com.example.reminder.models;

public class AllTasksModel {

    private String taskText;
    private String date;
    private String id;
    private boolean isCompleted;

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
    public boolean isCompleted() {
        return isCompleted;
    }
}
