package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.util.Date;

public class DateAttributeDTO {

    @Json(name = "id")
    private long id;

    @Json(name = "name")
    private String name;

    @Json(name = "value")
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}