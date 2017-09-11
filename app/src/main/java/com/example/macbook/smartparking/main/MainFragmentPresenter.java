package com.example.macbook.smartparking.main;

import android.content.Context;
import android.util.Log;

import com.example.macbook.smartparking.data.sensorInfo.MainAdminResponse;
import com.example.macbook.smartparking.data.sensorInfo.Sensor;
import com.example.macbook.smartparking.data.sensorInfo.SensorResponseInterface;
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

public class MainFragmentPresenter {

    private MainViewFragment mainview;

    public MainFragmentPresenter(MainViewFragment mainview) {
        this.mainview = mainview;
    }

    public void getData(Context context) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sparkingsystem.info/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        SensorResponseInterface restClient = retrofit.create(SensorResponseInterface.class);
        mainview.showLoading();
        Call<MainAdminResponse> call = restClient.getData();
        call.enqueue(new Callback<MainAdminResponse>() {
            @Override
            public void onResponse(Call<MainAdminResponse> call, Response<MainAdminResponse> response) {
                switch (response.code()) {
                    case 200:
                        MainAdminResponse data = response.body();
                        Log.d("data", data.toString());
                        List<Sensor> sensors = getLateSensor(data);
                        mainview.showDataFromServer(sensors);
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
            public void onFailure(Call<MainAdminResponse> call, Throwable t) {
                Log.d("data", "error");
                mainview.hideLoading();
            }
        });
    }

    public List<Sensor> getLateSensor(MainAdminResponse response) {
        List<Sensor> sensors = new ArrayList<>();
        for (Sensor sensor : response.getSensors()) {
            String myDateString = sensor.getTime();
            String[] arrayHours = myDateString.split("\\:");
            int hour = Integer.parseInt(arrayHours[0]);
            if (hour > 24) {
                sensors.add(sensor);
            }
        }
        return sensors;
    }
}
