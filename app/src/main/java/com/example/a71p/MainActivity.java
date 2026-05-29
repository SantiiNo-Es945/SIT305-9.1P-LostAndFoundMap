package com.example.a71p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button createAdvertButton, showAdvertsButton, showMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAdvertButton = findViewById(R.id.createAdvertButton);
        showAdvertsButton = findViewById(R.id.showAdvertsButton);
        showMapButton = findViewById(R.id.showMapButton);
        createAdvertButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAdvertActivity.class);
            startActivity(intent);
        });

        showAdvertsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdvertListActivity.class);
            startActivity(intent);
        });
        showMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });
    }
}