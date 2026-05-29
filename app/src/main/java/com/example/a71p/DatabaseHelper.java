package com.example.a71p;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    //schema
    private static final String DATABASE_NAME = "lost_found.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ADVERTS = "adverts";
    //constants
    private static final String COL_ID = "id";
    private static final String COL_TYPE = "type";
    private static final String COL_NAME = "name";
    private static final String COL_PHONE = "phone";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_CATEGORY = "category";
    private static final String COL_LOCATION = "location";
    private static final String COL_DATE = "date";
    private static final String COL_IMAGE_PATH = "image_path";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_LONGITUDE = "longitude";
    //constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ADVERTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TYPE + " TEXT, " +
                COL_NAME + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_IMAGE_PATH + " TEXT, " +
                "latitude REAL, " +
                "longitude REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVERTS);
        onCreate(db);
    }
    //inserting data, new lost/found post
    public boolean insertAdvert(String type, String name, String phone,
                                String description, String category,
                                String location, String date, String imagePath,
                                double latitude, double longitude) {
        //open db to add data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TYPE, type);
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_CATEGORY, category);
        values.put(COL_LOCATION, location);
        values.put(COL_DATE, date);
        values.put(COL_IMAGE_PATH, imagePath);
        values.put(COL_LATITUDE, latitude);
        values.put(COL_LONGITUDE, longitude);

        long result = db.insert(TABLE_ADVERTS, null, values);
        return result != -1;
    }
    //loads all post
    public ArrayList<Advert> getAllAdverts() {
        ArrayList<Advert> adverts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //retrieving data
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADVERTS + " ORDER BY " + COL_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Advert advert = new Advert(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_PATH))
                );
                advert.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUDE)));
                advert.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUDE)));
                adverts.add(advert);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return adverts;
    }
    //filter post by catehory
    public ArrayList<Advert> getAdvertsByCategory(String category) {
        ArrayList<Advert> adverts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_ADVERTS + " WHERE " + COL_CATEGORY + " = ? ORDER BY " + COL_ID + " DESC",
                new String[]{category}
        );

        if (cursor.moveToFirst()) {
            do {
                Advert advert = new Advert(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_PATH))
                );
                advert.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUDE)));
                advert.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUDE)));
                adverts.add(advert);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return adverts;
    }
    //deleting data, removing post after owner is found
    public void deleteAdvert(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADVERTS, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
}