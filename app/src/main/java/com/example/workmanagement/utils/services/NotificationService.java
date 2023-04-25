package com.example.workmanagement.utils.services;

import com.example.workmanagement.utils.dto.NotificationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {

    @GET("notifications")
    Call<List<NotificationDTO>> getNotifications();

    @PUT("invitations/{id}/accept")
    Call<NotificationDTO> acceptInvitation(@Path("id") long id);

    @PUT("invitations/{id}/reject")
    Call<NotificationDTO> rejectInvitation(@Path("id") long id);
}
