package com.example.a1221858_1220137_courseproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.InputStream;

// ProfileFragment lets the user view and update their profile info
public class ProfileFragment extends Fragment {

    // Request code for picking an image from gallery
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewProfilePic;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonChangePic;
    private Button buttonSave;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    // Stores the path of the selected profile picture
    private String profilePicPath = "";

    // Required empty constructor for fragments
    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Set up helpers
        databaseHelper = new DatabaseHelper(requireContext(), "TripApp.db", null, 1);
        sessionManager = SessionManager.getInstance(requireContext());

        // Connect views
        imageViewProfilePic    = view.findViewById(R.id.imageView_profilePic);
        editTextFirstName      = view.findViewById(R.id.editText_profile_firstName);
        editTextLastName       = view.findViewById(R.id.editText_profile_lastName);
        editTextPhone          = view.findViewById(R.id.editText_profile_phone);
        editTextPassword       = view.findViewById(R.id.editText_profile_password);
        editTextConfirmPassword = view.findViewById(R.id.editText_profile_confirmPassword);
        buttonChangePic        = view.findViewById(R.id.button_changePic);
        buttonSave             = view.findViewById(R.id.button_saveProfile);

        // Load current user info into the fields
        loadUserInfo();

        // Change photo button — open gallery picker
        buttonChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Save button — validate and update profile
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        return view;
    }

    // Read current user data from DB and fill the fields
    private void loadUserInfo() {
        int userId = sessionManager.getUserId();
        Cursor cursor = databaseHelper.getUserByEmailAndPassword(
                sessionManager.getEmail(),
                "" // we just need to find by email so password can be anything
        );

        // Better approach — search by ID
        Cursor allUsers = databaseHelper.getAllUsers();
        if (allUsers != null && allUsers.moveToFirst()) {
            do {
                int id = allUsers.getInt(allUsers.getColumnIndexOrThrow("ID"));
                if (id == userId) {
                    editTextFirstName.setText(allUsers.getString(allUsers.getColumnIndexOrThrow("FIRST_NAME")));
                    editTextLastName.setText(allUsers.getString(allUsers.getColumnIndexOrThrow("LAST_NAME")));
                    editTextPhone.setText(allUsers.getString(allUsers.getColumnIndexOrThrow("PHONE")));
                    profilePicPath = allUsers.getString(allUsers.getColumnIndexOrThrow("PROFILE_PIC"));

                    // Load profile picture if one was saved before
                    if (profilePicPath != null && !profilePicPath.isEmpty()) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeFile(profilePicPath);
                            if (bitmap != null) {
                                imageViewProfilePic.setImageBitmap(bitmap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            } while (allUsers.moveToNext());
            allUsers.close();
        }
    }

    // Validate inputs and save updated profile to DB
    private void saveProfile() {

        // Validate first name
        if (InputValidator.isEmpty(editTextFirstName)) {
            editTextFirstName.setError("First name is required");
            return;
        }
        if (editTextFirstName.getText().toString().trim().length() < 3) {
            editTextFirstName.setError("First name must be at least 3 characters");
            return;
        }

        // Validate last name
        if (InputValidator.isEmpty(editTextLastName)) {
            editTextLastName.setError("Last name is required");
            return;
        }
        if (editTextLastName.getText().toString().trim().length() < 3) {
            editTextLastName.setError("Last name must be at least 3 characters");
            return;
        }

        // Validate phone
        if (InputValidator.isEmpty(editTextPhone)) {
            editTextPhone.setError("Phone is required");
            return;
        }
        if (!InputValidator.isValidPhone(editTextPhone)) {
            editTextPhone.setError("Phone must be at least 10 digits");
            return;
        }

        String firstName = editTextFirstName.getText().toString().trim();
        String lastName  = editTextLastName.getText().toString().trim();
        String phone     = editTextPhone.getText().toString().trim();
        String newPassword = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Get current encrypted password from DB to keep if not changing
        String passwordToSave = getCurrentPassword();

        // Only update password if user typed something in the password field
        if (!newPassword.isEmpty()) {

            // Validate new password rules
            if (newPassword.length() < 6) {
                editTextPassword.setError("Password must be at least 6 characters");
                return;
            }
            if (!hasLetterAndNumber(newPassword)) {
                editTextPassword.setError("Password must contain a letter and a number");
                return;
            }

            // Confirm password must match
            if (!newPassword.equals(confirmPassword)) {
                editTextConfirmPassword.setError("Passwords do not match");
                return;
            }

            // Encrypt the new password before saving
            passwordToSave = PasswordEncryptor.encrypt(newPassword);
        }

        // Save all changes to database
        int userId = sessionManager.getUserId();
        databaseHelper.updateUser(userId, firstName, lastName, phone, passwordToSave, profilePicPath);

        Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }

    // Open the phone gallery so user can pick a profile picture
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Called when user picks an image from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == Activity.RESULT_OK
                && data != null) {

            Uri imageUri = data.getData();

            try {
                // Show the selected image in the ImageView
                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewProfilePic.setImageBitmap(bitmap);

                // Save the image path so we can store it in the database
                profilePicPath = imageUri.toString();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Could not load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Get the current encrypted password for this user from the database
    private String getCurrentPassword() {
        int userId = sessionManager.getUserId();
        Cursor cursor = databaseHelper.getAllUsers();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                if (id == userId) {
                    String password = cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD"));
                    cursor.close();
                    return password;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return "";
    }

    // Check password has at least one letter and one number
    private boolean hasLetterAndNumber(String password) {
        boolean hasLetter = false;
        boolean hasNumber = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c))  hasNumber = true;
        }
        return hasLetter && hasNumber;
    }
}