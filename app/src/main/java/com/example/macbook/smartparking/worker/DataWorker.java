package com.example.macbook.smartparking.worker;

import android.util.Log;

import com.example.macbook.smartparking.data.graphs.first.DataToGraph;
import com.example.macbook.smartparking.data.graphs.first.GraphByDayInterface;
import com.example.macbook.smartparking.data.graphs.first.GraphByDayResponse;
import com.example.macbook.smartparking.data.graphs.first.GraphByMonthInterface;
import com.example.macbook.smartparking.data.graphs.first.Hour;
import com.example.macbook.smartparking.data.graphs.third.Block;
import com.example.macbook.smartparking.data.graphs.third.BlockResponse;
import com.example.macbook.smartparking.data.graphs.third.ThirdGraphInterface;
import com.example.macbook.smartparking.mainfragment.MainViewFragment;
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
 * Created by skirk on 9/5/17.
 */

public class DataWorker {

    public static final String BASE_URL = "http://sparkingsystem.info/api/";
    public static final String PARAM_DATE = "date";
    public Gson gsonObject;
    public Retrofit retrofitObject;

    public void constructData(String baseUrl){
        gsonObject = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofitObject = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gsonObject))
                .build();
    }

    /**
     *  This method gives the data necesary to show the data for day
     * @param dateString this must be 2017-10-10 ... YYYY-MM-DD
     * @param mainView this is the interface that must show or dismis the data
     */
    public void getData(String dateString, final MainViewFragment mainView){
        constructData(BASE_URL);
        GraphByDayInterface restClient = retrofitObject.create(GraphByDayInterface.class);
        mainView.showLoading();
        DataToGraph graph = new DataToGraph(dateString);
        Call<GraphByDayResponse> call = restClient.postJson(graph);
        call.enqueue(new Callback<GraphByDayResponse>() {
            @Override
            public void onResponse(Call<GraphByDayResponse> call, Response<GraphByDayResponse> response) {
                switch (response.code()) {
                    case 200:
                        GraphByDayResponse data = response.body();
                        List<Float> sensors = new ArrayList<>();
                        for(Hour hour : data.getAnalytics().getHours()){
                            Float amount  = hour.getAmount().floatValue();
                            sensors.add(amount);
                        }
                        mainView.showDataFromServerCharset(sensors);
                        mainView.hideLoading();
                        break;
                    case 401:
                        mainView.hideLoading();
                        break;
                    default:
                        mainView.hideLoading();
                        break;
                }
            }
            @Override
            public void onFailure(Call<GraphByDayResponse> call, Throwable t) {
                mainView.hideLoading();
            }
        });
    }

    /**
     * This method help to show data from month it only need the date string
     * @param dateDesigned
     * @param mainView
     */

    public void getDataByMonth(String dateDesigned, final MainViewFragment mainView){
        constructData(BASE_URL);
        GraphByMonthInterface restClient = retrofitObject.create(GraphByMonthInterface.class);
        mainView.showLoading();

        // TO DO change this to get the param, it must change the fragment too

        DataToGraph graph = new DataToGraph(dateDesigned);
        Call<GraphByDayResponse> call = restClient.postJson(graph);
        call.enqueue(new Callback<GraphByDayResponse>() {
            @Override
            public void onResponse(Call<GraphByDayResponse> call, Response<GraphByDayResponse> response) {
                switch (response.code()) {
                    case 200:
                        GraphByDayResponse data = response.body();
                        List<Float> sets = new ArrayList<>();
                        for(Hour hour : data.getAnalytics().getHours()){
                            Float amount  = hour.getAmount().floatValue();
                            sets.add(amount);
                        }
                        mainView.showDataFromServerCharset(sets);
                        mainView.hideLoading();
                        break;
                    case 401:
                        mainView.hideLoading();
                        break;
                    default:
                        mainView.hideLoading();
                        break;
                }
            }
            @Override
            public void onFailure(Call<GraphByDayResponse> call, Throwable t) {
                Log.d("data", "error");
                mainView.hideLoading();
            }
        });
    }

    /**
     * This method helps to get the data by blocks of car
     * @param dateString the date of search in format 2017-10-10 ... YYYY-MM-DD
     * @param mainView
     */
    public void getDataByBlock(String dateString, final MainViewFragment mainView){
        constructData(BASE_URL);
        ThirdGraphInterface restClient = retrofitObject.create(ThirdGraphInterface.class);
        mainView.showLoading();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_DATE, dateString);
        Call<BlockResponse> call = restClient.getData(parameters);
        call.enqueue(new Callback<BlockResponse>() {
            @Override
            public void onResponse(Call<BlockResponse> call, Response<BlockResponse> response) {
                switch (response.code()) {
                    case 200:
                        BlockResponse data = response.body();
                        List<Float> sets = new ArrayList<>();
                        for(Block hour : data.getBlocks()){
                            Float amount  = hour.getMonthAmount().floatValue();
                            sets.add(amount);
                        }
                        mainView.showDataFromServerCharset(sets);
                        mainView.hideLoading();
                        break;
                    case 401:
                        mainView.hideLoading();
                        break;
                    default:
                        mainView.hideLoading();
                        break;
                }
            }

            @Override
            public void onFailure(Call<BlockResponse> call, Throwable t) {
                mainView.hideLoading();
            }
        });
    }
}
