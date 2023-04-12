package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

public class BoardInfo {

    @Json(name = "id")
    private Long id;

    @Json(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
