package com.example.a1221858_1220137_courseproject;

import android.content.Context;
import android.content.SharedPreferences;

// SessionManager is a Singleton class that saves the logged-in user info
public class SessionManager {

    // Name of the SharedPreferences file
    private static final String PREF_NAME = "TripAppSession";

    // Keys for storing user info
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID      = "userId";
    private static final String KEY_EMAIL        = "email";
    private static final String KEY_FIRST_NAME   = "firstName";
    private static final String KEY_IS_ADMIN     = "isAdmin";

    // Singleton instance
    private static SessionManager ourInstance = null;

    // SharedPreferences object and its editor
    private static SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    // Private constructor — sets up sharedPreferences and editor
    private SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // getInstance — returns the single instance (creates it if first time)
    public static SessionManager getInstance(Context context) {
        if (ourInstance != null) {
            return ourInstance;
        }
        ourInstance = new SessionManager(context);
        return ourInstance;
    }

    // Save user info to SharedPreferences when they log in
    // Uses editor.putString and editor
    public void saveLoginSession(int userId, String email, String firstName, int isAdmin) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID,          userId);
        editor.putString(KEY_EMAIL,         email);
        editor.putString(KEY_FIRST_NAME,    firstName);
        editor.putInt(KEY_IS_ADMIN,         isAdmin);
        editor.commit(); // save all changes
    }

    // Clear session when user logs out
    public void logout() {
        editor.clear();
        editor.commit();
    }

    // Check if a user is currently logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Getters — read saved values from SharedPreferences
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

    // Save the email when Remember Me is checked
    public void saveRememberedEmail(String email) {
        editor.putString("rememberedEmail", email);
        editor.commit();
    }

    // Clear the remembered email when Remember Me is unchecked
    public void clearRememberedEmail() {
        editor.remove("rememberedEmail");
        editor.commit();
    }

    // Get the remembered email — returns empty string if none saved
    public String getRememberedEmail() {
        return sharedPreferences.getString("rememberedEmail", "");
    }
}