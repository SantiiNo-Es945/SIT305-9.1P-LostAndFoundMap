package com.example.a71p;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class CreateAdvertActivity extends AppCompatActivity {

    RadioGroup typeRadioGroup;
    EditText nameEditText, phoneEditText, descriptionEditText, categoryEditText, locationEditText;
    Button selectImageButton, saveAdvertButton, getCurrentLocationButton;
    ImageView itemImageView;

    DatabaseHelper databaseHelper;
    String imagePath = "";
    double selectedLatitude = -37.8136;
    double selectedLongitude = 144.9631;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);
        //connects screen with SQLite
        databaseHelper = new DatabaseHelper(this);

        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        typeRadioGroup = findViewById(R.id.typeRadioGroup);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        locationEditText = findViewById(R.id.locationEditText);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveAdvertButton = findViewById(R.id.saveAdvertButton);
        getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton);
        itemImageView = findViewById(R.id.itemImageView);

        selectImageButton.setOnClickListener(v -> openGallery());

        saveAdvertButton.setOnClickListener(v -> saveAdvert());
        getCurrentLocationButton.setOnClickListener(v -> {

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

                        if (location != null) {

                            selectedLatitude = location.getLatitude();
                            selectedLongitude = location.getLongitude();

                            try {

                                Geocoder geocoder = new Geocoder(this);
                                List<Address> addresses =
                                        geocoder.getFromLocation(
                                                selectedLatitude,
                                                selectedLongitude,
                                                1
                                        );

                                if (addresses != null && !addresses.isEmpty()) {

                                    String address =
                                            addresses.get(0).getAddressLine(0);

                                    locationEditText.setText(address);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(
                                    this,
                                    "Current location selected",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        });
    }
    //open phone gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        imagePickerLauncher.launch(intent);
    }
    //receive selected image
    private final androidx.activity.result.ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri selectedImageUri = result.getData().getData();

                            getContentResolver().takePersistableUriPermission(
                                    selectedImageUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );

                            imagePath = selectedImageUri.toString();
                            itemImageView.setImageURI(selectedImageUri);
                        }
                    }
            );
    private String copyImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            File imageFile = new File(getFilesDir(), "advert_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return imageFile.getAbsolutePath();

        } catch (Exception e) {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            return "";
        }
    }
    //read all form fields
    private void saveAdvert() {
        int selectedId = typeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);

        String type = selectedRadioButton.getText().toString();
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        String location = locationEditText.getText().toString();
        //creates date/time stamp
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty()
                || category.isEmpty() || location.isEmpty() || imagePath.isEmpty()) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }
        //save advert into SQLite
        boolean inserted = databaseHelper.insertAdvert(
                type, name, phone, description, category, location, date, imagePath,
                selectedLatitude, selectedLongitude
        );

        if (inserted) {
            Toast.makeText(this, "Advert saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving advert", Toast.LENGTH_SHORT).show();
        }
    }
}