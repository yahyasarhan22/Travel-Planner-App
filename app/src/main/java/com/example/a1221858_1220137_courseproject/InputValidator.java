package com.example.a1221858_1220137_courseproject;

import android.widget.EditText;

public class InputValidator {

    public static boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    public static boolean isValidEmail(EditText editText) {
        String email = editText.getText().toString().trim();
        return email.contains("@") && email.contains(".");
    }

    public static boolean isValidPassword(EditText editText) {
        String password = editText.getText().toString().trim();
        return password.length() >= 6;
    }

    public static boolean isValidPhone(EditText editText) {
        String phone = editText.getText().toString().trim();
        return phone.length() >= 10;
    }

    public static boolean isValidQuantity(EditText editText) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) return false;
        try {
            int quantity = Integer.parseInt(value);
            return quantity > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
