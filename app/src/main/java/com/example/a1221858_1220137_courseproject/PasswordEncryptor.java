package com.example.a1221858_1220137_courseproject;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// PasswordEncryptor handles encrypting and decrypting passwords using AES
// AES (Advanced Encryption Standard) is stronger than MD5
// We use javax.crypto which is built into Android — no extra library needed
public class PasswordEncryptor {

    // Secret key must be exactly 16 characters for AES-128
    // This key is used to both encrypt and decrypt
    private static final String SECRET_KEY = "TripApp12345678!";

    // Encrypt a plain text password and return it as a Base64 string
    // Base64 makes the encrypted bytes safe to store as text in the database
    public static String encrypt(String plainPassword) {
        try {
            // Create the AES key from our secret key string
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            // Get an AES cipher instance
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // Set cipher to encrypt mode
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // Encrypt the password bytes
            byte[] encryptedBytes = cipher.doFinal(plainPassword.getBytes());

            // Convert encrypted bytes to a readable Base64 string
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            return plainPassword; // return plain if something goes wrong
        }
    }

    // Decrypt an encrypted password back to plain text
    // Used when we need to verify or display the password
    public static String decrypt(String encryptedPassword) {
        try {
            // Create the same AES key
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            // Get AES cipher instance
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // Set cipher to decrypt mode
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            // Decode Base64 back to encrypted bytes
            byte[] encryptedBytes = Base64.decode(encryptedPassword, Base64.DEFAULT);

            // Decrypt to get original password bytes
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return encryptedPassword; // return as-is if something goes wrong
        }
    }
}