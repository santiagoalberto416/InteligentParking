package com.example.macbook.smartparking.login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by santiagoalbertokirk on 12/10/17.
 */

public interface LoginUserInterface {
    @POST("login")
    Call<RegisterResponse> postJson(@Body LoginPostData data);
}
