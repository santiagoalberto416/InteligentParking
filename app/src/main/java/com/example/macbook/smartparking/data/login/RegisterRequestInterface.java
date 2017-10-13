package com.example.macbook.smartparking.data.login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by macbook on 11/07/17.
 */

public interface RegisterRequestInterface {
    @POST("saveOrEdit")
    Call<RegisterResponse> postJson(@Body RegisterPostData data);
}
