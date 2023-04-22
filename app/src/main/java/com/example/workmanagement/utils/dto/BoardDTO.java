package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class BoardDTO {

    @Json(name = "id")
    private long id;

    @Json(name = "name")
    private String name;

    @Json(name = "adminId")
    private long adminId;

    @Json(name = "membersIds")
    private List<Long> membersIds;

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

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public List<Long> getMembersIds() {
        return membersIds;
    }

    public void setMembersIds(List<Long> membersIds) {
        this.membersIds = membersIds;
    }

    public BoardDTO(String name, List<Long> membersIds) {
        this.name = name;
        this.membersIds = membersIds;
    }
}
