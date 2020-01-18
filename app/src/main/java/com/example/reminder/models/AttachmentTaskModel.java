package com.example.reminder.models;

import android.media.Image;

public class AttachmentTaskModel {
    String id;
    int image ;
    String subTaskTitle;

    public AttachmentTaskModel(String id, int image, String subTaskTitle) {
        this.id = id;
        this.image = image;
        this.subTaskTitle = subTaskTitle;
    }

    public String getSubTaskTitle() {
        return subTaskTitle;
    }

    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }
}
