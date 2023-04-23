package com.example.workmanagement.utils.services;

import com.example.workmanagement.utils.dto.BoardDTO;
import com.example.workmanagement.utils.dto.BoardDetailsDTO;
import com.example.workmanagement.utils.dto.BoardInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BoardService {

    @GET("boards/{id}")
    Call<BoardDetailsDTO> getBoardDetails(@Path("id") long id);

    @POST("boards")
    Call<BoardInfo> createBoard(@Body BoardDTO dto);
}
