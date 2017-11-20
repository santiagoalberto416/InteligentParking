package com.example.macbook.smartparking.service;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.SharedUtils;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class NotifyUserActivity extends AppCompatActivity {


    private Button mButtonOk;
    private Button mButtonCancel;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_user);
        mButtonOk = (Button)findViewById(R.id.ok);
        mButtonCancel = (Button)findViewById(R.id.cancel);
        try {
            mSocket = IO.socket(getApplicationContext().getString(R.string.socket_url));
            mSocket.connect();
        } catch (URISyntaxException e) {
            Log.d("socket", "isnt working the socket " + e.toString());
        }

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager manager = (NotificationManager) NotifyUserActivity.this.getSystemService(NotifyUserActivity.this.NOTIFICATION_SERVICE);
                manager.cancel(R.string.local_service_started);
                NotifyUserActivity.this.stopService(new Intent(NotifyUserActivity.this, ListenSocketService.class));
                mSocket.close();
                finish();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mIdOfUser = SharedUtils.getInstance().getUserId(NotifyUserActivity.this);
                int mSpotId = SharedUtils.getInstance().getSpot(NotifyUserActivity.this);
                mSocket.emit("notifySecurity", "{\"iduser\":"+mIdOfUser+", \"spot\":" + mSpotId + "}");
                NotificationManager manager = (NotificationManager) NotifyUserActivity.this.getSystemService(NotifyUserActivity.this.NOTIFICATION_SERVICE);
                manager.cancel(R.string.local_service_started);
                NotifyUserActivity.this.stopService(new Intent(NotifyUserActivity.this, ListenSocketService.class));
                mSocket.close();
                showSecurityMessage();
            }
        });
    }

    private void showSecurityMessage(){
        findViewById(R.id.message_of_confirmation).setVisibility(View.GONE);
        findViewById(R.id.buttonContainer).setVisibility(View.GONE);
        findViewById(R.id.message_of_security).setVisibility(View.VISIBLE);
    }
}
