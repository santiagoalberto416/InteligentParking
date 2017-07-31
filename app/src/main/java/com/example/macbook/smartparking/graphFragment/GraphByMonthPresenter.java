package com.example.macbook.smartparking.graphFragment;

import android.content.Context;
import android.util.Log;

import com.example.macbook.smartparking.data.graphs.first.DataToGraph;
import com.example.macbook.smartparking.data.graphs.first.GraphByDayInterface;
import com.example.macbook.smartparking.data.graphs.first.GraphByDayResponse;
import com.example.macbook.smartparking.data.graphs.first.GraphByMonthInterface;
import com.example.macbook.smartparking.data.graphs.first.Hour;
import com.example.macbook.smartparking.mainFragment.MainViewFragment;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macbook on 11/07/17.
 */

public class GraphByMonthPresenter {

    private MainViewFragment mainview;

    public GraphByMonthPresenter(MainViewFragment mainview) {
        this.mainview = mainview;
    }

    public void getData(Context context, String dateDesigned){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(dateDesigned)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GraphByMonthInterface restClient = retrofit.create(GraphByMonthInterface.class);
        mainview.showLoading();
        DataToGraph graph = new DataToGraph("2017-07-05");
        Call<GraphByDayResponse> call = restClient.postJson(graph);
        call.enqueue(new Callback<GraphByDayResponse>() {
            @Override
            public void onResponse(Call<GraphByDayResponse> call, Response<GraphByDayResponse> response) {
                switch (response.code()) {
                    case 200:
                        GraphByDayResponse data = response.body();
                        Log.d("data", data.toString());
                        List<Float> sensors = getCharsets(data);
                        mainview.showDataFromServerCharset(sensors);
                        break;
                    case 401:
                        Log.d("data", "error");
                        mainview.hideLoading();
                        break;
                    default:
                        Log.d("data", "error");
                        mainview.hideLoading();
                        break;
                }
            }

            @Override
            public void onFailure(Call<GraphByDayResponse> call, Throwable t) {
                Log.d("data", "error");
                mainview.hideLoading();
            }
        });
    }

    public List<Float> getCharsets(GraphByDayResponse response){
        List<Float> sets = new ArrayList<>();
        for(Hour hour : response.getAnalytics().getHours()){
            Float amount  = hour.getAmount().floatValue();
            sets.add(amount);
        }
        return sets;
    }
}
