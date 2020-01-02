package com.example.reminder.models;

public class MyAllTasksModel {

    private String notes;
    private String date;
    private String id;

    public MyAllTasksModel(String id, String notes, String date) {
        this.id = id;
        this.notes = notes;
        this.date = date;
    }

    public String getId() {return id; }
    public String getNotes() {
        return notes;
    }
    public String getDate() {
        return date;
    }

}
