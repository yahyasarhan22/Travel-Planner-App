package com.example.a1221858_1220137_courseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TripApp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER        = "USER";
    private static final String TABLE_TRIP        = "TRIP";
    private static final String TABLE_RESERVATION = "RESERVATION";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_USER + " (" +
                        "ID INTEGER PRIMARY KEY, " +
                        "EMAIL TEXT, " +
                        "FIRST_NAME TEXT, " +
                        "LAST_NAME TEXT, " +
                        "PASSWORD TEXT, " +
                        "GENDER TEXT, " +
                        "MAJOR TEXT, " +
                        "PHONE TEXT, " +
                        "PROFILE_PIC TEXT, " +
                        "IS_ADMIN INTEGER)"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_TRIP + " (" +
                        "ID INTEGER PRIMARY KEY, " +
                        "DESTINATION TEXT, " +
                        "DURATION TEXT, " +
                        "PRICE REAL, " +
                        "DESCRIPTION TEXT)"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_RESERVATION + " (" +
                        "ID INTEGER PRIMARY KEY, " +
                        "USER_ID INTEGER, " +
                        "TRIP_ID INTEGER, " +
                        "QUANTITY INTEGER, " +
                        "TYPE TEXT, " +
                        "DATE TEXT, " +
                        "STATUS TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);
        onCreate(sqLiteDatabase);
    }

    // USER methods
    public void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ID",          user.getId());
        contentValues.put("EMAIL",       user.getEmail());
        contentValues.put("FIRST_NAME",  user.getFirstName());
        contentValues.put("LAST_NAME",   user.getLastName());
        contentValues.put("PASSWORD",    user.getPassword());
        contentValues.put("GENDER",      user.getGender());
        contentValues.put("MAJOR",       user.getMajor());
        contentValues.put("PHONE",       user.getPhone());
        contentValues.put("PROFILE_PIC", user.getProfilePic());
        contentValues.put("IS_ADMIN",    user.getIsAdmin());

        sqLiteDatabase.insert(TABLE_USER, null, contentValues);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_USER, null);
    }

    public Cursor getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_USER +
                        " WHERE EMAIL = ? AND PASSWORD = ?",
                new String[]{email, password}
        );
    }

    // TRIP methods
    public void insertTrip(Trip trip) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ID",          trip.getId());
        contentValues.put("DESTINATION", trip.getDestination());
        contentValues.put("DURATION",    trip.getDuration());
        contentValues.put("PRICE",       trip.getPrice());
        contentValues.put("DESCRIPTION", trip.getDescription());

        sqLiteDatabase.insert(TABLE_TRIP, null, contentValues);
    }

    public Cursor getAllTrips() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TRIP, null);
    }

    // RESERVATION methods
    public void insertReservation(Reservation reservation) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ID",       reservation.getId());
        contentValues.put("USER_ID",  reservation.getUserId());
        contentValues.put("TRIP_ID",  reservation.getTripId());
        contentValues.put("QUANTITY", reservation.getQuantity());
        contentValues.put("TYPE",     reservation.getType());
        contentValues.put("DATE",     reservation.getDate());
        contentValues.put("STATUS",   reservation.getStatus());

        sqLiteDatabase.insert(TABLE_RESERVATION, null, contentValues);
    }

    public Cursor getAllReservations() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_RESERVATION, null);
    }

    public Cursor getReservationsByUserId(int userId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_RESERVATION + " WHERE USER_ID = ?",
                new String[]{String.valueOf(userId)}
        );
    }
}
