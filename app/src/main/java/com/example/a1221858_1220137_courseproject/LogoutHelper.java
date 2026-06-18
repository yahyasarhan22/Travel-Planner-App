package com.example.a1221858_1220137_courseproject;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// LogoutHelper handles the logout flow — shows a confirm dialog then clears session
// Can be called from any Activity that has a logout button in the Navigation Drawer
public class LogoutHelper {

    // Show a confirmation dialog before logging out
    // context must be an Activity context
    public static void showLogoutDialog(Context context) {

        new AlertDialog.Builder(context)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    logout(context);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    // Clear the session and go back to LoginActivity
    private static void logout(Context context) {

        // Clear all saved session data using SessionManager
        SessionManager sessionManager = SessionManager.getInstance(context);
        sessionManager.logout();

        // Go to LoginActivity and clear all activities behind it
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}