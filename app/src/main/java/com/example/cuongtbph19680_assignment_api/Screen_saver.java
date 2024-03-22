package com.example.cuongtbph19680_assignment_api;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Screen_saver extends AppCompatActivity {
    private static final long SPLASH_TIME_OUT = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.saver); // Set animation
        imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_animation));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Screen_saver.this, Screen_login.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}