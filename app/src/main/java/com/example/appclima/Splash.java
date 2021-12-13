package com.example.appclima;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final int DURACION = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Tu codigo aqui
                Intent intent = new Intent (Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },DURACION);
    }
}