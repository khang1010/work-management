package com.example.workmanagement.utils.services.impl;

import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.services.AuthInterceptor;
import com.example.workmanagement.utils.services.TableService;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class TableServiceImpl {

    private static TableServiceImpl tableService;

    private Moshi moshi = new Moshi.Builder().build();

    public static TableServiceImpl getInstance() {
        if(tableService == null)
            tableService = new TableServiceImpl();
        return tableService;
    }

    public TableService getService(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(token));
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient.build())
                .baseUrl(SystemConstant.BASE_URL).build();
        return retrofit.create(TableService.class);
    }

    private TableServiceImpl() {

    }
}
