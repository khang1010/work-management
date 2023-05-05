package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class UserDTO {

    @Json(name = "id")
    private long id;

    @Json(name = "token")
    private String token;

    @Json(name = "email")
    private String email;

    @Json(name = "displayName")
    private String displayName;

    @Json(name = "givenName")
    private String givenName;

    @Json(name = "familyName")
    private String familyName;

    @Json(name = "photoUrl")
    private String photoUrl;

    @Json(name = "boards")
    private List<BoardInfo> boards;

    @Json(name = "hasNonReadNotification")
    private boolean hasNonReadNotification;

    public boolean isHasNonReadNotification() {
        return hasNonReadNotification;
    }

    public void setHasNonReadNotification(boolean hasNonReadNotification) {
        this.hasNonReadNotification = hasNonReadNotification;
    }

    public List<BoardInfo> getBoards() {
        return boards;
    }

    public void setBoards(List<BoardInfo> boards) {
        this.boards = boards;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
