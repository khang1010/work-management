package com.example.workmanagement.utils.services;

import com.example.workmanagement.utils.dto.LabelDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LabelService {

    @POST("labels")
    Call<LabelDTO> createLabel(@Body LabelDTO dto);

    @PUT("labels/{id}")
    Call<LabelDTO> updateLabel(@Path("id") long id, @Body LabelDTO dto);

    @DELETE("labels/{id}")
    Call<Void> deleteLabel(@Path("id") long id);

}
