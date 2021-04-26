package com.ieti.myapplication.service;

import com.ieti.myapplication.model.LoginWrapper;
import com.ieti.myapplication.model.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/auth")
    Call<Token> login(@Body LoginWrapper loginWrapper);
}
