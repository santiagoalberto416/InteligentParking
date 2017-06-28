package com.example.macbook.smartparking.maps;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.data.map.RestTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.json.JSONObject;

public class FullMapActivity extends AppCompatActivity implements OnMapReadyCallback {



    ProgressDialog progress;

    private GoogleMap mapa;
    private JSONObject data;
    private GeoJsonLayer layer;


    public FullMapActivity() {
        // Required empty public constructor
    }



    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blank);

        MapWorkerSingleton.getInstance().getContent(getString(R.string.server_url)+"five", this);
        progress = ProgressDialog.show(this, "Getting Data ...", "Waiting For Results...", true);
        registerReceiver(receiver, new IntentFilter(MapWorkerSingleton.ACTION_FOR_INTENT_CALLBACK));
        MapFragment mapFragment = (MapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        registerReceiver(receiver, new IntentFilter(MapWorkerSingleton.ACTION_FOR_INTENT_CALLBACK));
    }


    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }
    /**
     * Our Broadcast Receiver. We get notified that the data is ready this way.
     */
    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (progress != null)
            {
                progress.dismiss();
                try {
                    String response = intent.getStringExtra(RestTask.HTTP_RESPONSE);
                    data = new JSONObject(response);
                    if(data!=null && mapa!=null){
                        layer = new GeoJsonLayer(mapa, data);
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.459509, -116.825864), 20));
                        for (GeoJsonFeature feature : layer.getFeatures()) {
                            if (feature.hasProperty("state")) {
                                Log.d("id",feature.getProperty("id"));
                                int state = Integer.parseInt(feature.getProperty("state"));

                                GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                                if(state == 1){
                                    style.setFillColor(Color.GREEN);
                                }else{
                                    style.setFillColor(Color.YELLOW);
                                }
                                feature.setPolygonStyle(style);
                                layer.addFeature(feature);
                            }
                        }
                        layer.addLayerToMap();
                    }
                }catch(JSONException ex){

                }

            }
        }
    };
}
