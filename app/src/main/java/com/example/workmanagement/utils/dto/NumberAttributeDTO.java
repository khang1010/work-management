package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class NumberAttributeDTO implements Serializable {

    @Json(name = "id")
    private long id;

    @Json(name = "name")
    private String name;

    @Json(name = "value")
    private long value;

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

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
