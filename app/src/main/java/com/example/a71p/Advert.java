package com.example.a71p;

public class Advert {

    private int id;
    private String type; // Lost or Found
    private String name;
    private String phone;
    private String description;
    private String category;
    private String location;
    private String date;
    private String imagePath;
    private double latitude;
    private double longitude;

    public Advert(int id, String type, String name, String phone,
                  String description, String category,
                  String location, String date, String imagePath) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.category = category;
        this.location = location;
        this.date = date;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getImagePath() { return imagePath; }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}