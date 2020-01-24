package com.example.reminder.models;

public class CompletedTasksModel {

    String taskTitle,TaskDate,id;

    public CompletedTasksModel(String taskTitle, String taskDate, String id) {
        this.taskTitle = taskTitle;
        TaskDate = taskDate;
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDate() {
        return TaskDate;
    }

    public String getId() {
        return id;
    }
}
