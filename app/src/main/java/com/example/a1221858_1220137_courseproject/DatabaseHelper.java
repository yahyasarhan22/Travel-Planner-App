package com.example.a1221858_1220137_courseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// DatabaseHelper extends SQLiteOpenHelper
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "TripApp.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USER        = "USER";
    private static final String TABLE_TRIP        = "TRIP";
    private static final String TABLE_RESERVATION = "RESERVATION";

    // Constructor
    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // onCreate — creates all three tables when the database is first made
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create USER table — ID is auto assigned by SQLite
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_USER + " (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

        // Create TRIP table
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_TRIP + " (" +
                        "ID INTEGER PRIMARY KEY, " +
                        "DESTINATION TEXT, " +
                        "DURATION TEXT, " +
                        "PRICE REAL, " +
                        "DESCRIPTION TEXT)"
        );

        // Create RESERVATION table — ID is auto assigned by SQLite
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_RESERVATION + " (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "USER_ID INTEGER, " +
                        "TRIP_ID INTEGER, " +
                        "QUANTITY INTEGER, " +
                        "TYPE TEXT, " +
                        "DATE TEXT, " +
                        "STATUS TEXT)"
        );

        // Insert the required admin account — project requires admin@admin.com / Admin123!
        // Password is encrypted with MD5 same as PasswordHelper.encrypt()
        String adminPassword = PasswordEncryptor.encrypt("Admin123!");
        sqLiteDatabase.execSQL(
                "INSERT INTO " + TABLE_USER +
                        " (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, GENDER, MAJOR, PHONE, PROFILE_PIC, IS_ADMIN)" +
                        " VALUES ('admin@admin.com', 'Admin', 'Admin', '" + adminPassword + "', 'Male', 'Admin', '0000000000', '', 1)"
        );
    }

    // onUpgrade — called if DATABASE_VERSION increases in the future
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop old tables and recreate — simple approach from lab manual
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);
        onCreate(sqLiteDatabase);
    }

    // USER methods
    // Insert a User into the USER table
    // We do NOT put the ID — SQLite assigns it automatically with AUTOINCREMENT
    public void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

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

    // Get all users — returns a Cursor, same as getAllCustomers() in lab manual
    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_USER, null);
    }

    // Get one user by email and password — used for login check
    public Cursor getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_USER +
                        " WHERE EMAIL = ? AND PASSWORD = ?",
                new String[]{email, password}
        );
    }

    // ===============================================================
    // TRIP methods
    // ===============================================================

    // Insert a Trip into the TRIP table
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

    // Get all trips — returns a Cursor
    public Cursor getAllTrips() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TRIP, null);
    }
    // RESERVATION methods

    // Insert a Reservation into the RESERVATION table
    // We do NOT put the ID — SQLite assigns it automatically with AUTOINCREMENT
    public void insertReservation(Reservation reservation) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("USER_ID",  reservation.getUserId());
        contentValues.put("TRIP_ID",  reservation.getTripId());
        contentValues.put("QUANTITY", reservation.getQuantity());
        contentValues.put("TYPE",     reservation.getType());
        contentValues.put("DATE",     reservation.getDate());
        contentValues.put("STATUS",   reservation.getStatus());

        sqLiteDatabase.insert(TABLE_RESERVATION, null, contentValues);
    }

    // Get all reservations — returns a Cursor
    public Cursor getAllReservations() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_RESERVATION, null);
    }

    // Get reservations for a specific user by their userId
    public Cursor getReservationsByUserId(int userId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_RESERVATION + " WHERE USER_ID = ?",
                new String[]{String.valueOf(userId)}
        );
    }
}