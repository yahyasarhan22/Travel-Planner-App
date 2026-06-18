package com.example.a1221858_1220137_courseproject;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "TripAppSession";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID      = "userId";
    private static final String KEY_EMAIL        = "email";
    private static final String KEY_FIRST_NAME   = "firstName";
    private static final String KEY_IS_ADMIN     = "isAdmin";
    private static final String KEY_REMEMBERED_EMAIL = "rememberedEmail";

    private static SessionManager ourInstance = null;
    private static SharedPreferences sharedPreferences = null;


    private SessionManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SessionManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SessionManager(context);
        }
        return ourInstance;
    }

    public void saveLoginSession(int userId, String email, String firstName, int isAdmin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID,          userId);
        editor.putString(KEY_EMAIL,         email);
        editor.putString(KEY_FIRST_NAME,    firstName);
        editor.putInt(KEY_IS_ADMIN,         isAdmin);
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_IS_LOGGED_IN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_FIRST_NAME);
        editor.remove(KEY_IS_ADMIN);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getFirstName() {
        return sharedPreferences.getString(KEY_FIRST_NAME, "");
    }

    public boolean isAdmin() {
        return sharedPreferences.getInt(KEY_IS_ADMIN, 0) == 1;
    }

    public void saveRememberedEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REMEMBERED_EMAIL, email);
        editor.apply();
    }

    public void clearRememberedEmail() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_REMEMBERED_EMAIL);
        editor.apply();
    }

    public String getRememberedEmail() {
        return sharedPreferences.getString(KEY_REMEMBERED_EMAIL, "");
    }
}