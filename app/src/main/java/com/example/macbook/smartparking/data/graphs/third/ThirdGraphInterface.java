package com.example.macbook.smartparking.data.graphs.third;

import com.example.macbook.smartparking.data.graphs.second.SecondGraphInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by macbook on 05/08/17.
 */

public interface ThirdGraphInterface {
    @POST("blockActivities")
    Call<BlockResponse> getData(@Body HashMap<String, String> parameters);
}
