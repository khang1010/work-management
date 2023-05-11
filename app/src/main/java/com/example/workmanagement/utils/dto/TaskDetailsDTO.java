package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class TaskDetailsDTO {

    @Json(name = "id")
    private long id;

    @Json(name = "status")
    private String status;

    @Json(name = "user")
    private UserInfoDTO user;

    @Json(name = "textAttributes")
    private List<TextAttributeDTO> textAttributes;

    @Json(name = "numberAttributes")
    private List<NumberAttributeDTO> numberAttributes;

    @Json(name = "dateAttributes")
    private List<DateAttributeDTO> dateAttributes;

    @Json(name = "labelAttributes")
    private List<LabelAttributeDTO> labelAttributes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserInfoDTO getUser() {
        return user;
    }

    public void setUser(UserInfoDTO user) {
        this.user = user;
    }

    public List<TextAttributeDTO> getTextAttributes() {
        return textAttributes;
    }

    public void setTextAttributes(List<TextAttributeDTO> textAttributes) {
        this.textAttributes = textAttributes;
    }

    public List<NumberAttributeDTO> getNumberAttributes() {
        return numberAttributes;
    }

    public void setNumberAttributes(List<NumberAttributeDTO> numberAttributes) {
        this.numberAttributes = numberAttributes;
    }

    public List<DateAttributeDTO> getDateAttributes() {
        return dateAttributes;
    }

    public void setDateAttributes(List<DateAttributeDTO> dateAttributes) {
        this.dateAttributes = dateAttributes;
    }

    public List<LabelAttributeDTO> getLabelAttributes() {
        return labelAttributes;
    }

    public void setLabelAttributes(List<LabelAttributeDTO> labelAttributes) {
        this.labelAttributes = labelAttributes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
