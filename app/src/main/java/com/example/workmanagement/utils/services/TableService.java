package com.example.workmanagement.utils.services;

import com.example.workmanagement.utils.dto.TableDTO;
import com.example.workmanagement.utils.dto.TableDetailsDTO;
import com.example.workmanagement.utils.dto.TaskDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TableService {

    @POST("tasks")
    Call<TableDetailsDTO> createTable(@Body TableDTO dto);

    @PUT("tasks/{id}")
    Call<TableDetailsDTO> updateTable(@Path("id") long id, @Body TaskDTO dto);

    @DELETE("tasks/{id}")
    Call<Void> deleteTask(@Path("id") long id);

}
