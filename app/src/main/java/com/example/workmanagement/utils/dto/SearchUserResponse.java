package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class SearchUserResponse {

    @Json(name = "page")
    private int page;

    @Json(name = "users")
    private List<UserInfoDTO> users;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<UserInfoDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoDTO> users) {
        this.users = users;
    }
}
