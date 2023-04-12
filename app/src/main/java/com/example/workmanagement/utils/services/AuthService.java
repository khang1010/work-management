package com.example.workmanagement.utils.services;

import com.example.workmanagement.utils.dto.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    Call<UserDTO> loginUser(@Body UserDTO userDTO);
}
