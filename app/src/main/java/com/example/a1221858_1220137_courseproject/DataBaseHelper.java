package com.example.a1221858_1220137_courseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.a1221858_1220137_courseproject.Trip;
import com.example.a1221858_1220137_courseproject.User;
import com.example.a1221858_1220137_courseproject.Reservation;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravelPlanner.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_TRIPS = "trips";
    public static final String TABLE_RESERVATIONS = "reservations";
    public static final String TABLE_FAVORITES = "favorites";

    public static final String KEY_ID = "id";

    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_FIRST_NAME = "first_name";
    public static final String KEY_USER_LAST_NAME = "last_name";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USER_MAJOR = "major";
    public static final String KEY_USER_PHONE = "phone";
    public static final String KEY_USER_PROFILE_PIC = "profile_pic";
    public static final String KEY_USER_IS_ADMIN = "is_admin";

    public static final String KEY_TRIP_DESTINATION = "destination";
    public static final String KEY_TRIP_COUNTRY = "country";
    public static final String KEY_TRIP_DURATION = "duration_days";
    public static final String KEY_TRIP_PRICE = "price";
    public static final String KEY_TRIP_RATING = "rating";
    public static final String KEY_TRIP_DESCRIPTION = "description";
    public static final String KEY_TRIP_IMAGE_URL = "image_url";

    public static final String KEY_RES_USER_ID = "user_id";
    public static final String KEY_RES_TRIP_ID = "trip_id";
    public static final String KEY_RES_QUANTITY = "quantity";
    public static final String KEY_RES_TYPE = "type";
    public static final String KEY_RES_DATE = "date";
    public static final String KEY_RES_STATUS = "status";

    public static final String KEY_FAV_USER_ID = "user_id";
    public static final String KEY_FAV_TRIP_ID = "trip_id";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_EMAIL + " TEXT UNIQUE,"
                + KEY_USER_FIRST_NAME + " TEXT,"
                + KEY_USER_LAST_NAME + " TEXT,"
                + KEY_USER_PASSWORD + " TEXT,"
                + KEY_USER_GENDER + " TEXT,"
                + KEY_USER_MAJOR + " TEXT,"
                + KEY_USER_PHONE + " TEXT,"
                + KEY_USER_PROFILE_PIC + " TEXT,"
                + KEY_USER_IS_ADMIN + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_TRIPS_TABLE = "CREATE TABLE " + TABLE_TRIPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TRIP_DESTINATION + " TEXT,"
                + KEY_TRIP_COUNTRY + " TEXT,"
                + KEY_TRIP_DURATION + " INTEGER,"
                + KEY_TRIP_PRICE + " REAL,"
                + KEY_TRIP_RATING + " REAL,"
                + KEY_TRIP_DESCRIPTION + " TEXT,"
                + KEY_TRIP_IMAGE_URL + " TEXT" + ")";
        db.execSQL(CREATE_TRIPS_TABLE);

        String CREATE_RESERVATIONS_TABLE = "CREATE TABLE " + TABLE_RESERVATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_RES_USER_ID + " INTEGER,"
                + KEY_RES_TRIP_ID + " INTEGER,"
                + KEY_RES_QUANTITY + " INTEGER,"
                + KEY_RES_TYPE + " TEXT,"
                + KEY_RES_DATE + " TEXT,"
                + KEY_RES_STATUS + " TEXT,"
                + "FOREIGN KEY(" + KEY_RES_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_RES_TRIP_ID + ") REFERENCES " + TABLE_TRIPS + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_RESERVATIONS_TABLE);

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_FAV_USER_ID + " INTEGER,"
                + KEY_FAV_TRIP_ID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_FAV_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_FAV_TRIP_ID + ") REFERENCES " + TABLE_TRIPS + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void insertTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, trip.getId());
        values.put(KEY_TRIP_DESTINATION, trip.getDestination());
        values.put(KEY_TRIP_COUNTRY, trip.getCountry());
        values.put(KEY_TRIP_DURATION, trip.getDurationDays());
        values.put(KEY_TRIP_PRICE, trip.getPrice());
        values.put(KEY_TRIP_RATING, trip.getRating());
        values.put(KEY_TRIP_DESCRIPTION, trip.getDescription());
        values.put(KEY_TRIP_IMAGE_URL, trip.getImageUrl());

        db.insertWithOnConflict(TABLE_TRIPS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<Trip> getAllTrips() {
        List<Trip> tripList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRIPS, null);

        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip();
                trip.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                trip.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TRIP_DESTINATION)));
                trip.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TRIP_COUNTRY)));
                trip.setDurationDays(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TRIP_DURATION)));
                trip.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TRIP_PRICE)));
                trip.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TRIP_RATING)));
                trip.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TRIP_DESCRIPTION)));
                trip.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TRIP_IMAGE_URL)));
                tripList.add(trip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tripList;
    }

    public void deleteTrip(int tripId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIPS, KEY_ID + " = ?", new String[]{String.valueOf(tripId)});
        db.close();
    }

    public void updateTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TRIP_DESTINATION, trip.getDestination());
        values.put(KEY_TRIP_COUNTRY, trip.getCountry());
        values.put(KEY_TRIP_DURATION, trip.getDurationDays());
        values.put(KEY_TRIP_PRICE, trip.getPrice());
        values.put(KEY_TRIP_RATING, trip.getRating());
        values.put(KEY_TRIP_DESCRIPTION, trip.getDescription());
        values.put(KEY_TRIP_IMAGE_URL, trip.getImageUrl());

        db.update(TABLE_TRIPS, values, KEY_ID + " = ?", new String[]{String.valueOf(trip.getId())});
        db.close();
    }

    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_FIRST_NAME, user.getFirstName());
        values.put(KEY_USER_LAST_NAME, user.getLastName());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        values.put(KEY_USER_GENDER, user.getGender());
        values.put(KEY_USER_MAJOR, user.getMajor());
        values.put(KEY_USER_PHONE, user.getPhone());
        values.put(KEY_USER_PROFILE_PIC, user.getProfilePic());
        values.put(KEY_USER_IS_ADMIN, user.getIsAdmin());

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_EMAIL + " = ?", new String[]{email});
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_IS_ADMIN + " = 0", null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_EMAIL)));
                user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_LAST_NAME)));
                user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_GENDER)));
                user.setMajor(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_MAJOR)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_PHONE)));
                user.setProfilePic(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_PROFILE_PIC)));
                user.setIsAdmin(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_IS_ADMIN)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
}