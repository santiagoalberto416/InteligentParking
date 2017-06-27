package com.example.macbook.smartparking.mainFragment;

import com.example.macbook.smartparking.data.sensorInfo.MainAdminResponse;
import com.example.macbook.smartparking.data.sensorInfo.Sensor;

import java.util.List;

/**
 * Created by macbook on 26/06/17.
 */

public interface MainViewFragment {

    void hideLoading();

    void showLoading();

    void showDataFromServer(List<Sensor> response);

    void showDataFromServerCharset(List<Float> chartSets);

}
