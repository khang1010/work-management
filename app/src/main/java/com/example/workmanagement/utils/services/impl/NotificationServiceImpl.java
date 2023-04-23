package com.example.workmanagement.utils.services.impl;

import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.services.AuthInterceptor;
import com.example.workmanagement.utils.services.BoardService;
import com.example.workmanagement.utils.services.NotificationService;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class NotificationServiceImpl {

    private static NotificationServiceImpl notificationService;

    private Moshi moshi = new Moshi.Builder().build();

    public static NotificationServiceImpl getInstance() {
        if(notificationService == null)
            notificationService = new NotificationServiceImpl();
        return notificationService;
    }

    public NotificationService getService(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(token));
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient.build())
                .baseUrl(SystemConstant.BASE_URL).build();
        return retrofit.create(NotificationService.class);
    }

    private NotificationServiceImpl() {

    }
}
