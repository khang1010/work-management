package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class TableDetailsDTO {

    @Json(name = "id")
    private long id;

    @Json(name = "createdBy")
    private UserInfoDTO createdBy;

    @Json(name = "name")
    private String name;

    @Json(name = "description")
    private String description;


    @Json(name = "members")
    private List<UserInfoDTO> members;

    @Json(name = "tasks")
    private List<TaskDetailsDTO> tasks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserInfoDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserInfoDTO createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserInfoDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserInfoDTO> members) {
        this.members = members;
    }

    public List<TaskDetailsDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDetailsDTO> tasks) {
        this.tasks = tasks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
