package com.example.workmanagement.utils.services.impl;

import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.services.AuthInterceptor;
import com.example.workmanagement.utils.services.NotificationService;
import com.example.workmanagement.utils.services.TaskService;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class TaskServiceImpl {

    private static TaskServiceImpl taskService;

    private Moshi moshi = new Moshi.Builder().build();

    public static TaskServiceImpl getInstance() {
        if(taskService == null)
            taskService = new TaskServiceImpl();
        return taskService;
    }

    public TaskService getService(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(token));
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient.build())
                .baseUrl(SystemConstant.BASE_URL).build();
        return retrofit.create(TaskService.class);
    }

    private TaskServiceImpl() {

    }
}
