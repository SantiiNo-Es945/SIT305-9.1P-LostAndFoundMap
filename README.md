# SIT305 Task 9.1P – Lost and Found Map App

## Overview
This application allows users to post lost and found items.
Each advert includes a description, image, category, location, and an automatically generated date and time.

The application also integrates Google Maps, allowing users to save the location of an item and view lost and found items on a map.

## Features
- Create new lost or found adverts
- Upload an image for each item
- View all adverts in a list
- Search/filter adverts by category
- View detailed information for each advert
- Remove adverts when items are found
- Get the user's current location
- Display adverts on Google Maps
- Search for adverts within a specified radius

## Technologies Used
- Java
- Android Studio
- SQLite
- RecyclerView
- Google Maps API
- Fused Location Provider

## How it works
The app stores all adverts locally using SQLite. Users can create adverts, upload images, and save their current location.

All adverts are displayed using a RecyclerView and can be filtered by category. Users can also view adverts on a Google Map, where markers are placed based on the stored coordinates.

The radius search feature allows users to find items located within a specified distance from their current location.

## How to run the app
1. Open the project in Android Studio.
2. Configure a valid Google Maps API key.
3. Build and run the application.
