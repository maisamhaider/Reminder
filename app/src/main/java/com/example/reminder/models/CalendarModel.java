package com.example.reminder.models;

public class CalendarModel {

        public   String haeder_or_child;
       public  Integer type;

    public CalendarModel(String haeder_or_child, Integer type) {
        this.haeder_or_child = haeder_or_child;
        this.type = type;
    }

    public String getHaeder_or_child() {
        return haeder_or_child;
    }

    public Integer getType() {
        return type;
    }

    public void setHaeder_or_child(String haeder_or_child) {
        this.haeder_or_child = haeder_or_child;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
