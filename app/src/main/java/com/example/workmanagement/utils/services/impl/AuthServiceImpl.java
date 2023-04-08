package com.example.workmanagement.utils.services.impl;

import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.services.AuthService;
import com.squareup.moshi.Moshi;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class AuthServiceImpl {

    private static AuthServiceImpl authService;

    private Moshi moshi = new Moshi.Builder().build();

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(SystemConstant.BASE_URL).build();

    public static AuthServiceImpl getInstance() {
        if(authService == null)
            authService = new AuthServiceImpl();
        return authService;
    }

    public AuthService getService() {
        return retrofit.create(AuthService.class);
    }

    private AuthServiceImpl() {

    }

}
