package com.example.workmanagement.utils.services.impl;

import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.services.AuthInterceptor;
import com.example.workmanagement.utils.services.BoardService;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class BoardServiceImpl {

    private static BoardServiceImpl boardService;

    private Moshi moshi = new Moshi.Builder().build();

    public static BoardServiceImpl getInstance() {
        if(boardService == null)
            boardService = new BoardServiceImpl();
        return boardService;
    }

    public BoardService getService(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(token));
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient.build())
                .baseUrl(SystemConstant.BASE_URL).build();
        return retrofit.create(BoardService.class);
    }

    private BoardServiceImpl() {

    }
}
