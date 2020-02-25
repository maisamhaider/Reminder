package com.example.reminder.models;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventModel  implements Comparable<EventModel> {
    String calendarId;
    String nameOfEvent;
    String startDates;
    String descriptions;
    String location;


    public EventModel() {
    }

    public EventModel(String calendarId, String nameOfEvent, String startDates, String descriptions, String location) {
        this.calendarId = calendarId;
        this.nameOfEvent = nameOfEvent;
        this.startDates = startDates;
        this.descriptions = descriptions;
        this.location = location;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getNameOfEvent() {
        return nameOfEvent;
    }

    public void setNameOfEvent(String nameOfEvent) {
        this.nameOfEvent = nameOfEvent;
    }

    public String getStartDates() {
        return startDates;
    }
    @SuppressLint("NewApi")
    public Date getDate() {
        @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat format = new SimpleDateFormat(
                "EEEE, d MMMM"    );
        Date date = new Date(  );
        try {
            date = format.parse( getStartDates());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date ;
    }

    public void setStartDates(String startDates) {
        this.startDates = startDates;
    }


    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int compareTo(EventModel o) {
        return getDate().compareTo(o.getDate());
    }
}
