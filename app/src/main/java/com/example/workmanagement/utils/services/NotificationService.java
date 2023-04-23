package com.example.workmanagement.utils.services;

import com.example.workmanagement.utils.dto.NotificationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotificationService {

    @GET("notifications")
    Call<List<NotificationDTO>> getNotifications();
}
