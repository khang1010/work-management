package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

public class LabelDTO {

    @Json(name = "id")
    private long id;

    @Json(name = "name")
    private String name;

    @Json(name = "color")
    private String color;

    @Json(name = "boardId")
    private long boardId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }
}
