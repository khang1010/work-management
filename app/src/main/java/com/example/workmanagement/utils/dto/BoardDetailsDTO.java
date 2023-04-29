package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

public class BoardDetailsDTO {

    @Json(name = "id")
    private long id;

    @Json(name = "name")
    private String name;

    @Json(name = "admin")
    private UserInfoDTO admin;

    @Json(name = "members")
    private List<UserInfoDTO> members;

    @Json(name = "tables")
    private List<TableDetailsDTO> tables;

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

    public UserInfoDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserInfoDTO admin) {
        this.admin = admin;
    }

    public List<UserInfoDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserInfoDTO> members) {
        this.members = members;
    }

    public List<TableDetailsDTO> getTables() {
        return tables;
    }

    public void setTables(List<TableDetailsDTO> tables) {
        this.tables = tables;
    }
}
