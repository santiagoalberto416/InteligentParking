package com.example.macbook.smartparking.maps;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.macbook.smartparking.OptionSpotParking;
import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.SharedUtils;
import com.example.macbook.smartparking.data.map.RestTask;
import com.example.macbook.smartparking.service.ListenSocketService;
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
    private View confirmationView;
    private Marker marker;
    private Button cancelAction;
    private Button acceptButton;
    private int mIdInUserinUse;
    private int mIdSpotInUse;
    public final static String USER_ID = "id_user";
    static final String SPOT_IS_OCUPATED = "com.example.ocupated";


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
        mIdInUserinUse = SharedUtils.getInstance().getUserId(this);
        try {
            mSocket = IO.socket(getApplicationContext().getString(R.string.server_url));
        } catch (URISyntaxException e) {
        }
        setContentView(R.layout.fragment_blank);
        confirmationView = findViewById(R.id.confirmation);
        selectSpotView = findViewById(R.id.background);
        selectSpotView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSpotOptions();
            }
        });
        cancelAction = (Button)findViewById(R.id.cancel_action) ;
        acceptButton = (Button)findViewById(R.id.accept_action) ;
        acceptButton.setOnClickListener((View view) -> {
            cancelSpotOptions();
            Intent i= new Intent(MapInteractionActivity.this, ListenSocketService.class);
            i.putExtra(ListenSocketService.SPOT_ID, mIdSpotInUse);
            i.putExtra(ListenSocketService.USER_ID, mIdInUserinUse);
            MapInteractionActivity.this.startService(i);
            mSocket.on("notifySpot"+SharedUtils.getInstance().getUserId(MapInteractionActivity.this), onSpotSelected);
            confirmationView.setVisibility(View.VISIBLE);
        });
        cancelAction.setOnClickListener((View view)-> {
                cancelSpotOptions();
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
        if(!isMyServiceRunning(ListenSocketService.class)){
            confirmationView.setVisibility(View.GONE);
        }else{
            confirmationView.setVisibility(View.VISIBLE);
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
                                if(id!=0 && mIdInUserinUse!=0) {
                                        MapInteractionActivity.this.selectSpotView.setVisibility(View.VISIBLE);
                                        LatLng latLng = getFirstLocation(id);
                                        MarkerOptions options = new MarkerOptions()
                                                .position(latLng)
                                                .title("Cajon numero " + id);
                                        mIdSpotInUse = id;
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



    private Emitter.Listener onSpotSelected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final Object[] arg = args;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject((String) arg[0]);
                        Log.d("data socket", data.toString());
                        int state;
                        try {
                            state = data.getInt("state");
                            if(state == 0){
                                cancelSpotOptions();
                                MapInteractionActivity.this.stopService(new Intent(MapInteractionActivity.this, ListenSocketService.class));
                                Toast.makeText(MapInteractionActivity.this, "Este espacio ya fue registrado por otro usuario, consulte al administrador", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            return;
                        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (SharedUtils.getInstance().getUserId(this)!=0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            MenuItem item = menu.getItem(0);
            Drawable drawable = item.getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mybutton:
                SharedUtils.getInstance().setUserId(this, 0);
                SharedUtils.getInstance().setUserType(this, 0);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void cancelSpotOptions(){
        selectSpotView.setVisibility(View.GONE);
        if(marker!=null){
            marker.remove();
        }
    }

}
