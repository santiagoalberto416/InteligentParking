package com.example.macbook.smartparking.data.graphs.second;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by macbook on 26/06/17.
 */

public interface SecondGraphInterface {
    @GET("three")
    Call<SecondGraphInterface> getData();
}
