package com.example.greenscene.Models.Database;

public class PhotoElement {
    private String eventId;
    private String pictureId;
    private String userId;

    public PhotoElement(String eventId, String pictureId, String userId) {
        this.eventId = eventId;
        this.pictureId = pictureId;
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
