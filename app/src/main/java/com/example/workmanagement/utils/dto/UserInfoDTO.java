package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

public class UserInfoDTO {

    @Json(name = "id")
    private long id;

    @Json(name = "email")
    private String email;

    @Json(name = "displayName")
    private String displayName;

    @Json(name = "photoUrl")
    private String photoUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
