package com.example.reminder.models;

public class AttachmentTaskModel {
    String id;
    int image ;
    String taskAttachmentTitle;
    String createDate;

    public AttachmentTaskModel(String id, int image, String taskAttachmentTitle,String createDate) {
        this.id = id;
        this.image = image;
        this.taskAttachmentTitle = taskAttachmentTitle;
        this.createDate = createDate;
    }

    public String getTaskAttachmentTitle() {
        return taskAttachmentTitle;
    }

    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getCreateDate() {
        return createDate;
    }
}
