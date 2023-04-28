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

    @POST("tables")
    Call<TableDetailsDTO> createTable(@Body TableDTO dto);

    @PUT("tables/{id}")
    Call<TableDetailsDTO> updateTable(@Path("id") long id, @Body TableDTO dto);

    @DELETE("tables/{id}")
    Call<Void> deleteTask(@Path("id") long id);

}
