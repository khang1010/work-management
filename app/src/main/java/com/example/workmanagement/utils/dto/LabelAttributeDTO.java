package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class LabelAttributeDTO implements Serializable {

    @Json(name = "id")
    private Long id;

    @Json(name = "name")
    private String name;

    @Json(name = "labelId")
    private long labelId;

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

    public long getLabelId() {
        return labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }
}
