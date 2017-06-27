package com.example.macbook.smartparking.data.sensorInfo;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by macbook on 26/06/17.
 */

public interface FirstGraphInterface {
    @GET("two")
    Call<FirstGraphResponse> getData();
}
