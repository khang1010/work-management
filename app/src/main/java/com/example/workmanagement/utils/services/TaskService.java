package com.example.workmanagement.utils.services;

import com.example.workmanagement.utils.dto.TaskDTO;
import com.example.workmanagement.utils.dto.TaskDetailsDTO;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskService {

    @POST("tasks")
    Call<TaskDetailsDTO> createTask(TaskDTO dto);

    @PUT("tasks/{id}")
    Call<TaskDetailsDTO> updateTask(@Path("id") long id, TaskDTO dto);
}
