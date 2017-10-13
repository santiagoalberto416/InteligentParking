package com.example.macbook.smartparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.macbook.smartparking.maps.FullMapActivity;
import com.example.macbook.smartparking.maps.MapInteractionActivity;
import com.example.macbook.smartparking.service.ListenSocketService;

public class SplashScreen extends AppCompatActivity {

    Button continueButton;
    Button continueButtonAdministrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        continueButton = (Button) findViewById(R.id.continueButton);
        continueButtonAdministrator = (Button) findViewById(R.id.continueButtonAdmin);

        /// intent service
        Intent intentService = new Intent(this, ListenSocketService.class);
        intentService.putExtra(ListenSocketService.USER_ID, "test user name");
        startService(intentService);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        continueButtonAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(SplashScreen.this, MapInteractionActivity.class);
                startActivity(loginIntent);
            }
        });

    }
}
