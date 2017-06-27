package com.example.macbook.smartparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    Button continueButton;
    Button continueButtonAdministrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        continueButton = (Button)findViewById(R.id.continueButton);
        continueButtonAdministrator = (Button)findViewById(R.id.continueButtonAdmin);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        continueButtonAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent =  new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

    }
}
