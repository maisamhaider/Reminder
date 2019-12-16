package com.example.reminder.models;

public class InputRemiderModel {

    String text;
    int icon;

    public InputRemiderModel(String text,int icon) {
        this.text = text;
        this.icon= icon;
    }

    public String getText() {
        return text;
    }

    public int getIcon() {
        return icon;
    }
}
