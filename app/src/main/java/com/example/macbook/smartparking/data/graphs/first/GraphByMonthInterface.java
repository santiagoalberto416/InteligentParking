package com.example.macbook.smartparking.data.graphs.first;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by macbook on 11/07/17.
 */

public interface GraphByMonthInterface {
    @POST("activitiesByMonth")
    Call<GraphByDayResponse> postJson(@Body DataToGraph body);
}
