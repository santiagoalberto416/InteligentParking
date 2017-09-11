package com.example.macbook.smartparking.maps;

import android.app.Activity;
import android.util.Log;

import com.example.macbook.smartparking.data.map.RestTask;

import org.apache.http.client.methods.HttpGet;

import java.net.URI;

/**
 * Created by skirk on 6/27/17.
 */

public class MapWorkerSingleton {

    public static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_COMMUNICATE";


    private static MapWorkerSingleton instance = null;

    protected MapWorkerSingleton() {
        // Exists only to defeat instantiation.
    }

    public static MapWorkerSingleton getInstance() {
        if (instance == null) {
            instance = new MapWorkerSingleton();
        }
        return instance;
    }

    public void getContent(String url, Activity activity) {
        try {
            HttpGet httpGet = new HttpGet(new URI(url));
            RestTask task = new RestTask(activity, ACTION_FOR_INTENT_CALLBACK);
            task.execute(httpGet);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }

}
