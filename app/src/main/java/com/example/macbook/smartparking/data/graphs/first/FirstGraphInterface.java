package com.example.macbook.smartparking.data.graphs.first;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by skirk on 6/27/17.
 */

public interface FirstGraphInterface {
        @GET("two")
        Call<FirstGraphResponse> getData();
}
