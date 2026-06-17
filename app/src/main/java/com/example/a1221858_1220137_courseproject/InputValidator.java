package com.example.a1221858_1220137_courseproject;

import android.widget.EditText;

// InputValidator is a helper class with static methods
// to check user inputs before saving or sending them
public class InputValidator {

    // Check if an EditText field is empty
    // Returns true if it is empty, false if it has text
    public static boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    // Check if an email looks valid (must contain @ and .)
    public static boolean isValidEmail(EditText editText) {
        String email = editText.getText().toString().trim();
        return email.contains("@") && email.contains(".");
    }

    // Check if password is long enough (at least 6 characters)
    public static boolean isValidPassword(EditText editText) {
        String password = editText.getText().toString().trim();
        return password.length() >= 6;
    }

    // Check if phone number has at least 10 digits
    public static boolean isValidPhone(EditText editText) {
        String phone = editText.getText().toString().trim();
        return phone.length() >= 10;
    }

    // Check if a quantity field has a number greater than zero
    public static boolean isValidQuantity(EditText editText) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) return false;
        try {
            int quantity = Integer.parseInt(value);
            return quantity > 0;
        } catch (NumberFormatException e) {
            return false; // not a number at all
        }
    }
}