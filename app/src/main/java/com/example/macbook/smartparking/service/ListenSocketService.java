package com.example.macbook.smartparking.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.macbook.smartparking.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by skirk on 9/11/17.
 */

public class ListenSocketService extends Service {

    /**
     * this service starts when a user select a spot to see changes
     * in their car
      */

    private static final String TAG = "socketservice";
    private NotificationManager mNM;
    private Socket mSocket;
    private String mNameOfUser;
    private int mSpotId;
    public static final String USER_ID = "userid";
    public static final String SPOT_ID = "spotid";
    public static String YES_ACTION = "yesaction";
    public static String NO_ACTION = "noaction";
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        ListenSocketService getService() {
            return ListenSocketService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Display a notification about us starting.  We put an icon in the status bar.
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        try {
            mSocket = IO.socket(getApplicationContext().getString(R.string.socket_url));
            mNameOfUser = intent.getStringExtra(USER_ID);
            mSpotId = intent.getIntExtra(SPOT_ID, 0);
            mSocket.connect();
            registerUser(mNameOfUser, mSpotId);
            mSocket.on(mNameOfUser, onNewMessage);
        } catch (URISyntaxException e) {
            Log.d(TAG, "isnt working the socket " + e.toString());
        }
        return START_NOT_STICKY;
    }

    private void registerUser(String userid, int spotId) {
        try {
            JSONObject registerData = new JSONObject();
            registerData.put("iduser", userid);
            registerData.put("idspace", spotId);
            mSocket.emit("registerUser", registerData);
        }catch (JSONException ex){
            Log.d(TAG, "isnt work making jsonobject");
        }
    }

    private void notifyToSecurity(String userid, int spotId){
        try {
            JSONObject registerData = new JSONObject();
            registerData.put("iduser", userid);
            registerData.put("idspace", spotId);
            mSocket.emit("notifySecurity", registerData);
        }catch (JSONException ex){
            Log.d(TAG, "isnt work making jsonobject");
        };
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final Object[] arg = args;
            showNotification();
        }
    };


    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder mBinder = new LocalBinder();
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        Intent resultIntent = new Intent(this, NotifyUserActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // Set the info for the views that show in the notification panel.
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_action_local_parking_black)
                        .setContentTitle("SmartParking")
                        .setContentIntent(resultPendingIntent)
                        .setContentText("Tu carro esta saliendo, Eres tu?");

        // Send the notification.
        mNM.notify(NOTIFICATION, mBuilder.build());
    }
}

