package com.sdtechnocrat.webviewapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.imageView);

        //imageView.setImageDrawable(getResources().getDrawable(R.drawable.splash_image));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getPackageName().equalsIgnoreCase("com.ntpl.moontvdigital")) {
                    startActivity(new Intent(SplashActivity.this, VideoActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}