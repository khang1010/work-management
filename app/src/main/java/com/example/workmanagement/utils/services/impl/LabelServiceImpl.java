package com.example.workmanagement.utils.services.impl;

import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.services.AuthInterceptor;
import com.example.workmanagement.utils.services.LabelService;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class LabelServiceImpl {

    private static LabelServiceImpl labelService;

    private Moshi moshi = new Moshi.Builder().build();

    public static LabelServiceImpl getInstance() {
        if(labelService == null)
            labelService = new LabelServiceImpl();
        return labelService;
    }

    public LabelService getService(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(token));
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient.build())
                .baseUrl(SystemConstant.BASE_URL).build();
        return retrofit.create(LabelService.class);
    }

    private LabelServiceImpl() {

    }
}
