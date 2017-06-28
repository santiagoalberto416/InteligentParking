package com.example.macbook.smartparking.graphFragment;

import android.content.Context;
import android.util.Log;

import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.data.graphs.first.FirstGraphInterface;
import com.example.macbook.smartparking.data.graphs.second.SecondGraphInterface;
import com.example.macbook.smartparking.data.graphs.first.FirstGraphResponse;
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
 * Created by macbook on 26/06/17.
 */

public class GraphPresenter {

    private MainViewFragment mainview;

    public GraphPresenter(MainViewFragment mainview) {
        this.mainview = mainview;
    }

    public void getData(Context context){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FirstGraphInterface restClient = retrofit.create(FirstGraphInterface.class);
        mainview.showLoading();
        Call<FirstGraphResponse> call = restClient.getData();
        call.enqueue(new Callback<FirstGraphResponse>() {
            @Override
            public void onResponse(Call<FirstGraphResponse> call, Response<FirstGraphResponse> response) {
                switch (response.code()) {
                    case 200:
                        FirstGraphResponse data = response.body();
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
            public void onFailure(Call<FirstGraphResponse> call, Throwable t) {
                Log.d("data", "error");
                mainview.hideLoading();
            }
        });
    }

    public List<Float> getCharsets(FirstGraphResponse response){
        List<Float> sets = new ArrayList<>();
        for(Hour hour : response.getAnalytics().getHours()){
            Float amount  = (float)hour.getAmount();
            sets.add(amount);
        }
        return sets;
    }
}
