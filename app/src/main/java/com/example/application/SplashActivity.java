package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {

    private Button btnsplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btnsplash=findViewById(R.id.button5);

        SharedPreferences sharedPreferences = getSharedPreferences("locale", Context.MODE_PRIVATE);
        boolean isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true);

        if (isFirstLaunch) {
            btnsplash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            });


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("is_first_launch", false);
            editor.apply();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }




    }
}