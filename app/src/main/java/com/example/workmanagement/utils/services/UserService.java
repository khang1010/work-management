package com.example.workmanagement.utils.services;

import com.example.workmanagement.utils.dto.SearchUserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("user")
    Call<SearchUserResponse> searchUser(@Query("page") int page, @Query("keyword") String keyword);
}
