package com.example.a1221858_1220137_courseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TripApp.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USER        = "USER";
    private static final String TABLE_TRIP        = "TRIP";
    private static final String TABLE_RESERVATION = "RESERVATION";
    private static final String TABLE_FAVORITE    = "FAVORITE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static class ReservationDetail {
        public int id;
        public int quantity;
        public String type;
        public String date;
        public String status;
        public String userName;
        public String userEmail;
        public String tripDestination;
        public String tripCountry;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
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

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_TRIP + " (" +
                        "ID INTEGER PRIMARY KEY, " +
                        "DESTINATION TEXT, " +
                        "DURATION TEXT, " +
                        "PRICE REAL, " +
                        "DESCRIPTION TEXT, " +
                        "COUNTRY TEXT, " +
                        "RATING REAL, " +
                        "IMAGE_URL TEXT)"
        );

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

        String adminPassword = PasswordEncryptor.encrypt("Admin123!");
        sqLiteDatabase.execSQL(
                "INSERT INTO " + TABLE_USER +
                        " (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, GENDER, MAJOR, PHONE, PROFILE_PIC, IS_ADMIN)" +
                        " VALUES ('admin@admin.com', 'Admin', 'Admin', '" + adminPassword + "', 'Male', 'Admin', '0000000000', '', 1)"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_FAVORITE + " (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "USER_ID INTEGER, " +
                        "TRIP_ID INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
                    user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")));
                    user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("FIRST_NAME")));
                    user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("LAST_NAME")));
                    user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD")));
                    user.setGender(cursor.getString(cursor.getColumnIndexOrThrow("GENDER")));
                    user.setMajor(cursor.getString(cursor.getColumnIndexOrThrow("MAJOR")));
                    user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("PHONE")));
                    user.setProfilePic(cursor.getString(cursor.getColumnIndexOrThrow("PROFILE_PIC")));
                    user.setIsAdmin(cursor.getInt(cursor.getColumnIndexOrThrow("IS_ADMIN")));
                    users.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return users;
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USER, "ID = ?", new String[]{String.valueOf(userId)});
    }

    public void updateUser(int userId, String firstName, String lastName,
                           String phone, String password, String profilePic) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FIRST_NAME",  firstName);
        contentValues.put("LAST_NAME",   lastName);
        contentValues.put("PHONE",       phone);
        contentValues.put("PASSWORD",    password);
        contentValues.put("PROFILE_PIC", profilePic);
        sqLiteDatabase.update(TABLE_USER, contentValues, "ID = ?",
                new String[]{String.valueOf(userId)});
    }

    public Cursor getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_USER +
                        " WHERE EMAIL = ? AND PASSWORD = ?",
                new String[]{email, password}
        );
    }

    public void insertTrip(Trip trip) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID",          trip.getId());
        contentValues.put("DESTINATION", trip.getDestination());
        contentValues.put("DURATION",    trip.getDuration());
        contentValues.put("PRICE",       trip.getPrice());
        contentValues.put("DESCRIPTION", trip.getDescription());
        contentValues.put("COUNTRY",     trip.getCountry());
        contentValues.put("RATING",      trip.getRating());
        contentValues.put("IMAGE_URL",   trip.getImageUrl());
        sqLiteDatabase.insert(TABLE_TRIP, null, contentValues);
    }

    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRIP, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Trip trip = new Trip();
                    trip.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
                    trip.setDestination(cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION")));
                    trip.setDurationF(cursor.getString(cursor.getColumnIndexOrThrow("DURATION")));
                    trip.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE")));
                    trip.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")));
                    trip.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY")));
                    trip.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow("RATING")));
                    trip.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow("IMAGE_URL")));
                    trips.add(trip);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return trips;
    }

    public void updateTrip(Trip trip) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESTINATION", trip.getDestination());
        contentValues.put("DURATION",    trip.getDuration());
        contentValues.put("PRICE",       trip.getPrice());
        contentValues.put("DESCRIPTION", trip.getDescription());
        contentValues.put("COUNTRY",     trip.getCountry());
        contentValues.put("IMAGE_URL",   trip.getImageUrl());
        db.update(TABLE_TRIP, contentValues, "ID = ?", new String[]{String.valueOf(trip.getId())});
    }

    public void deleteTrip(int tripId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TRIP, "ID = ?", new String[]{String.valueOf(tripId)});
    }

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

    public Cursor getReservationsByUserId(int userId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_RESERVATION + " WHERE USER_ID = ?",
                new String[]{String.valueOf(userId)}
        );
    }

    public void addFavorite(int userId, int tripId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_ID", userId);
        contentValues.put("TRIP_ID", tripId);
        sqLiteDatabase.insert(TABLE_FAVORITE, null, contentValues);
    }

    public void removeFavorite(int userId, int tripId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(
                TABLE_FAVORITE,
                "USER_ID = ? AND TRIP_ID = ?",
                new String[]{String.valueOf(userId), String.valueOf(tripId)}
        );
    }

    public List<ReservationDetail> getAllReservationsWithDetails() {
        List<ReservationDetail> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT r.ID, r.QUANTITY, r.TYPE, r.DATE, r.STATUS, " +
                "u.FIRST_NAME, u.LAST_NAME, u.EMAIL, " +
                "t.DESTINATION, t.COUNTRY " +
                "FROM RESERVATION r " +
                "INNER JOIN USER u ON r.USER_ID = u.ID " +
                "INNER JOIN TRIP t ON r.TRIP_ID = t.ID";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ReservationDetail detail = new ReservationDetail();
                    detail.id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                    detail.quantity = cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY"));
                    detail.type = cursor.getString(cursor.getColumnIndexOrThrow("TYPE"));
                    detail.date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                    detail.status = cursor.getString(cursor.getColumnIndexOrThrow("STATUS"));

                    String firstName = cursor.getString(cursor.getColumnIndexOrThrow("FIRST_NAME"));
                    String lastName = cursor.getString(cursor.getColumnIndexOrThrow("LAST_NAME"));
                    detail.userName = firstName + " " + lastName;
                    detail.userEmail = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));

                    detail.tripDestination = cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION"));
                    detail.tripCountry = cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY"));

                    list.add(detail);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    public boolean isFavorite(int userId, int tripId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_FAVORITE + " WHERE USER_ID = ? AND TRIP_ID = ?",
                new String[]{String.valueOf(userId), String.valueOf(tripId)}
        );
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public Cursor getFavoritesByUserId(int userId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_FAVORITE + " WHERE USER_ID = ?",
                new String[]{String.valueOf(userId)}
        );
    }
}
