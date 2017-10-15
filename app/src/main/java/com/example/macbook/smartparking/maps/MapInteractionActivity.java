package com.example.macbook.smartparking.maps;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.macbook.smartparking.OptionSpotParking;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MapInteractionActivity extends AppCompatActivity implements OnMapReadyCallback {


    ProgressDialog progress;

    private GoogleMap mapa;
    private JSONObject data;
    private GeoJsonLayer layer;
    private FloatingActionButton button;
    private View selectSpotView;
    private Marker marker;
    private Button cancelAction;


    public MapInteractionActivity() {
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
        } catch (URISyntaxException e) {
        }
        setContentView(R.layout.fragment_blank);
        selectSpotView = findViewById(R.id.background);
        selectSpotView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSpotOptions();
            }
        });
        cancelAction = (Button)findViewById(R.id.cancel_action) ;
        cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSpotOptions();
            }
        });
        MapWorkerSingleton.getInstance().getContent("http://sparkingsystem.info/api/geojson", this);
        progress = ProgressDialog.show(this, "Getting Data ...", "Waiting For Results...", true);
        registerReceiver(receiver, new IntentFilter(MapWorkerSingleton.ACTION_FOR_INTENT_CALLBACK));
        MapFragment mapFragment = (MapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        button = (FloatingActionButton) findViewById(R.id.instructionsButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MapInteractionActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.instructions_layout);
                dialog.show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(MapWorkerSingleton.ACTION_FOR_INTENT_CALLBACK));
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /**
     * Our Broadcast Receiver. We get notified that the data is ready this way.
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
                try {
                    String response = intent.getStringExtra(RestTask.HTTP_RESPONSE);
                    data = new JSONObject(response);
                    if (data != null && mapa != null) {
                        layer = new GeoJsonLayer(mapa, data);
                        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
                            @Override
                            public void onFeatureClick(GeoJsonFeature geoJsonFeature) {
                                int id = Integer.parseInt(geoJsonFeature.getProperty("id"));
                                if(id!=0) {
                                    MapInteractionActivity.this.selectSpotView.setVisibility(View.VISIBLE);
                                    LatLng latLng = getFirstLocation(id);
                                    MarkerOptions options = new MarkerOptions()
                                            .position(latLng)
                                            .title("Cajon numero " + id);
                                    marker = mapa.addMarker(options);
                                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                                }
                            }
                        });
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.459445, -116.825916), 19));
                        for (GeoJsonFeature feature : layer.getFeatures()) {
                            if (feature.hasProperty("state")) {
                                Log.d("id", feature.getProperty("id"));
                                String state = feature.getProperty("state").toString();
                                GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                                if (state.equals("free")) {
                                    style.setFillColor(ContextCompat.getColor(MapInteractionActivity.this, android.R.color.holo_green_dark));
                                } else {
                                    style.setFillColor(ContextCompat.getColor(MapInteractionActivity.this, android.R.color.holo_red_dark));
                                }
                                feature.setPolygonStyle(style);
                                layer.addFeature(feature);
                            }
                        }
                        layer.addLayerToMap();
                        mSocket.connect();
                        mSocket.on("sendChangeToDash", onNewMessage);
                    }
                } catch (JSONException ex) {

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


                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    };

    private void changeStateToFeature(int id, String state) {
        if (mapa != null) {
            for (GeoJsonFeature feature : layer.getFeatures()) {
                if (feature.hasProperty("id")) {
                    Log.d("id", feature.getProperty("id"));
                    int idFeature = Integer.parseInt(feature.getProperty("id"));
                    if (id == idFeature) {
                        GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                        if (state.equals("free")) {
                            style.setFillColor(ContextCompat.getColor(MapInteractionActivity.this, android.R.color.holo_green_dark));
                        } else if (state.equals("ocupated")) {
                            style.setFillColor(ContextCompat.getColor(MapInteractionActivity.this, android.R.color.holo_red_dark));
                        } else if (state.equals("parking")) {
                            style.setFillColor(Color.YELLOW);
                        }
                        feature.setPolygonStyle(style);
                        layer.addFeature(feature);
                    }
                }
            }
        }
    }


    public LatLng getFirstLocation(int id){
        LatLng latLng = new LatLng(0, 0);
        try {
            JSONArray array = data.getJSONArray("features");
            for (int i=0; i < array.length(); i++) {
                JSONObject feature = array.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");
                int idToCompare = properties.getInt("id");
                if(id == idToCompare){
                    JSONObject geometry = feature.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    JSONArray firstArray = (JSONArray)coordinates.get(0);
                    JSONArray firstPair = (JSONArray)firstArray.get(0);
                    latLng = new LatLng(firstPair.getDouble(1),firstPair.getDouble(0));
                    Log.d("find", "find_it");
                    return latLng;
                }
            }
        }catch (JSONException ex){

        }
        return null;
    }


    public void cancelSpotOptions(){
        selectSpotView.setVisibility(View.GONE);
        if(marker!=null){
            marker.remove();
        }
    }

}
