package com.example.a71p;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdvertListActivity extends AppCompatActivity {

    RecyclerView advertsRecyclerView;
    EditText filterEditText;
    Button filterButton, showAllButton;
    //reads data from SQLite
    DatabaseHelper databaseHelper;
    AdvertAdapter advertAdapter;
    ArrayList<Advert> advertList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_list);

        databaseHelper = new DatabaseHelper(this);
        //display the advert list
        advertsRecyclerView = findViewById(R.id.advertsRecyclerView);
        filterEditText = findViewById(R.id.filterEditText);
        filterButton = findViewById(R.id.filterButton);
        showAllButton = findViewById(R.id.showAllButton);
        //makes the list vertical
        advertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadAllAdverts();
        //category filter
        filterButton.setOnClickListener(v -> {
            String category = filterEditText.getText().toString();
            advertList = databaseHelper.getAdvertsByCategory(category);
            advertAdapter = new AdvertAdapter(this, advertList);
            advertsRecyclerView.setAdapter(advertAdapter);
        });

        showAllButton.setOnClickListener(v -> loadAllAdverts());
    }
    //get every saved advert
    private void loadAllAdverts() {
        advertList = databaseHelper.getAllAdverts();
        advertAdapter = new AdvertAdapter(this, advertList);
        advertsRecyclerView.setAdapter(advertAdapter);
    }
}