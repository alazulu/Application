package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.application.models.SplashAdapter;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private ViewPager2 vpsplash;
    private Button btnsplash;
    private SplashAdapter adapter=new SplashAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_Theme_Application);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        vpsplash=findViewById(R.id.viewpagersplash);
        btnsplash=findViewById(R.id.btnsplash);
        vpsplash.setAdapter(adapter);

        HashMap<String, Integer> item = new HashMap<>();
        item.put("text", R.string.splash1);
        item.put("image", R.drawable.scshot1);
        adapter.addSplashAdapter(item);


        item = new HashMap<>();
        item.put("text", R.string.splash2);
        item.put("image", R.drawable.scshot2);
        adapter.addSplashAdapter(item);


        item = new HashMap<>();
        item.put("text", R.string.splash3);
        item.put("image", R.drawable.scshot3);
        adapter.addSplashAdapter(item);



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