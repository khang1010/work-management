package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

public class NotificationDTO {

    @Json(name = "id")
    private long id;

    @Json(name = "message")
    private String message;

    @Json(name = "thumbnail")
    private String thumbnail;

    @Json(name = "isRead")
    private boolean isRead;

    @Json(name = "boardId")
    private long boardId;

    @Json(name = "type")
    private int type;

    @Json(name = "isAccept")
    private boolean isAccept;

    @Json(name = "isReject")
    private boolean isReject;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }

    public boolean isReject() {
        return isReject;
    }

    public void setReject(boolean reject) {
        isReject = reject;
    }
}
