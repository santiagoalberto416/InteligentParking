package com.example.macbook.smartparking.graphFragment;

import android.content.Context;
import android.util.Log;

import com.example.macbook.smartparking.data.graphs.first.DataToGraph;
import com.example.macbook.smartparking.data.graphs.first.GraphByDayResponse;
import com.example.macbook.smartparking.data.graphs.first.GraphByMonthInterface;
import com.example.macbook.smartparking.data.graphs.first.Hour;
import com.example.macbook.smartparking.data.graphs.second.SecondGraphInterface;
import com.example.macbook.smartparking.data.graphs.third.Block;
import com.example.macbook.smartparking.data.graphs.third.BlockResponse;
import com.example.macbook.smartparking.data.graphs.third.ThirdGraphInterface;
import com.example.macbook.smartparking.mainFragment.MainViewFragment;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macbook on 05/08/17.
 */

public class GraphByBlockPresenter {
    private MainViewFragment mainview;

    public GraphByBlockPresenter(MainViewFragment mainview) {
        this.mainview = mainview;
    }

    public void getData(Context context, String baseURl, String dateString){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ThirdGraphInterface restClient = retrofit.create(ThirdGraphInterface.class);
        mainview.showLoading();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("date", dateString);
        Call<BlockResponse> call = restClient.getData(parameters);
        call.enqueue(new Callback<BlockResponse>() {
            @Override
            public void onResponse(Call<BlockResponse> call, Response<BlockResponse> response) {
                switch (response.code()) {
                    case 200:
                        BlockResponse data = response.body();
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
            public void onFailure(Call<BlockResponse> call, Throwable t) {
                Log.d("data", "error");
                mainview.hideLoading();
            }
        });
    }

    public List<Float> getCharsets(BlockResponse response){
        List<Float> sets = new ArrayList<>();
        for(Block hour : response.getBlocks()){
            Float amount  = hour.getMonthAmount().floatValue();
            sets.add(amount);
        }
        return sets;
    }
}