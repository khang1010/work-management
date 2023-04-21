package com.example.workmanagement.utils.services;

import com.example.workmanagement.utils.dto.BoardDetailsDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BoardService {

    @GET("boards/{id}")
    Call<BoardDetailsDTO> getBoardDetails(@Path("id") long id);
}
