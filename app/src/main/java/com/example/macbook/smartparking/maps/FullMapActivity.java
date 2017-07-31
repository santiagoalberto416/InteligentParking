package com.example.macbook.smartparking.maps;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.data.map.RestTask;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
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

import java.net.URISyntaxException;

public class FullMapActivity extends AppCompatActivity implements OnMapReadyCallback {



    ProgressDialog progress;

    private GoogleMap mapa;
    private JSONObject data;
    private GeoJsonLayer layer;
    private FloatingActionButton button;


    public FullMapActivity() {
        // Required empty public constructor
    }

    private Socket mSocket;

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket(getApplicationContext().getString(R.string.socket_url));
        } catch (URISyntaxException e) {}
        setContentView(R.layout.fragment_blank);
        MapWorkerSingleton.getInstance().getContent("http://sparkingsystem.info/api/geojson", this);
        progress = ProgressDialog.show(this, "Getting Data ...", "Waiting For Results...", true);
        registerReceiver(receiver, new IntentFilter(MapWorkerSingleton.ACTION_FOR_INTENT_CALLBACK));
        MapFragment mapFragment = (MapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        button = (FloatingActionButton)findViewById(R.id.instructionsButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(FullMapActivity.this);
                dialog.setContentView(R.layout.instructions_layout);
                dialog.show();
            }
        });

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
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.460266, -116.825946), 18));
                        for (GeoJsonFeature feature : layer.getFeatures()) {
                            if (feature.hasProperty("state")) {
                                Log.d("id",feature.getProperty("id"));
                                int state = Integer.parseInt(feature.getProperty("state"));

                                GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                                if(state == 1){
                                    style.setFillColor(Color.parseColor("#A6BBC6CE"));
                                }else{
                                    style.setFillColor(Color.parseColor("#A6E33232"));
                                }
                                feature.setPolygonStyle(style);
                                layer.addFeature(feature);
                            }
                        }
                        layer.addLayerToMap();
                        mSocket.connect();
                        mSocket.on("sendChangeToDash", onNewMessage);
                    }
                }catch(JSONException ex){

                }

            }
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final Object[] arg = args;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                    JSONObject data = new JSONObject((String) arg[0]);
                    Log.d("data socket", data.toString());
                    int id;
                    String state;
                    try {
                        id = data.getInt("id");
                        state = data.getString("state");
                    } catch (JSONException e) {
                        return;
                    }
                    changeStateToFeature(id, state);
                    // add the message to view


                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
                }
            });
        }
    };

    private void changeStateToFeature(int id, String state){
        if(mapa!=null){
            for (GeoJsonFeature feature : layer.getFeatures()) {
                if (feature.hasProperty("id")) {
                    Log.d("id",feature.getProperty("id"));
                    int idFeature = Integer.parseInt(feature.getProperty("id"));
                    if(id==idFeature){
                        GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                        if(state.equals("free")){
                            style.setFillColor(Color.parseColor("#A6BBC6CE"));
                        }else if(state.equals("ocupated")){
                            style.setFillColor(Color.parseColor("#A6E33232"));
                        }else if(state.equals("parking")){
                            style.setFillColor(Color.YELLOW);
                        }
                        feature.setPolygonStyle(style);
                        layer.addFeature(feature);
                    }
                }
            }
        }
    }
}
