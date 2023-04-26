package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

public class MessageDTO {

    @Json(name = "email")
    private String email;

    @Json(name = "photoUrl")
    private String photoUrl;

    @Json(name = "message")
    private String message;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
