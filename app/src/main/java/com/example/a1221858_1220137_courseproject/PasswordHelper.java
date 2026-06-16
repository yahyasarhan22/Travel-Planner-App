package com.example.a1221858_1220137_courseproject;

import java.security.MessageDigest;

// A simple helper class to encrypt passwords using MD5
// This way we never store plain text passwords in the database
public class PasswordHelper {

    // Takes a plain password string and returns its MD5 hash
    public static String encrypt(String password) {
        try {
            // Get MD5 digest instance
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Convert password string to bytes and hash it
            byte[] hashBytes = md.digest(password.getBytes());

            // Convert the byte array into a readable hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            // If something goes wrong, return the original password
            e.printStackTrace();
            return password;
        }
    }
}