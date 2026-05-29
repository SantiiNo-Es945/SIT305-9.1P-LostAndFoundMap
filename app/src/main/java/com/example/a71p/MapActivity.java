package com.example.a71p;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    EditText radiusEditText;
    Button searchRadiusButton;
    ArrayList<Advert> adverts;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        radiusEditText = findViewById(R.id.radiusEditText);
        searchRadiusButton = findViewById(R.id.searchRadiusButton);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        searchRadiusButton.setOnClickListener(v -> searchByRadius());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        adverts = databaseHelper.getAllAdverts();

        for (Advert advert : adverts) {
            LatLng location = new LatLng(
                    advert.getLatitude(),
                    advert.getLongitude()
            );

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(location)
                            .title(advert.getName())
                            .snippet(advert.getCategory())
            );
        }

        LatLng melbourne = new LatLng(-37.8136, 144.9631);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, 10));
    }

    private void searchByRadius() {

        if (googleMap == null) {
            Toast.makeText(this, "Map is not ready yet", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radiusEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter radius in km", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location == null) {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    googleMap.clear();

                    double userLatitude = location.getLatitude();
                    double userLongitude = location.getLongitude();

                    double radiusKm = Double.parseDouble(
                            radiusEditText.getText().toString()
                    );

                    DatabaseHelper databaseHelper = new DatabaseHelper(this);
                    adverts = databaseHelper.getAllAdverts();

                    for (Advert advert : adverts) {

                        float[] results = new float[1];

                        Location.distanceBetween(
                                userLatitude,
                                userLongitude,
                                advert.getLatitude(),
                                advert.getLongitude(),
                                results
                        );

                        float distanceKm = results[0] / 1000;

                        if (distanceKm <= radiusKm) {
                            LatLng advertLocation = new LatLng(
                                    advert.getLatitude(),
                                    advert.getLongitude()
                            );

                            googleMap.addMarker(
                                    new MarkerOptions()
                                            .position(advertLocation)
                                            .title(advert.getName())
                                            .snippet(distanceKm + " km away")
                            );
                        }
                    }

                    LatLng userLocation = new LatLng(userLatitude, userLongitude);

                    googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(userLocation, 12)
                    );
                });
    }
}