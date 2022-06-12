package com.example.greenscene.Models.Database;

public class FavouriteElement {

    private String favElementId;

    private String userId;

    public FavouriteElement(String favElementId, String userId) {
        this.favElementId = favElementId;
        this.userId = userId;
    }

    public String getFavElementId() {
        return favElementId;
    }

    public void setFavElementId(String favElementId) {
        this.favElementId = favElementId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
