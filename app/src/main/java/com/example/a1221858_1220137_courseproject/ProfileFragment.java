package com.example.a1221858_1220137_courseproject;

import android.app.Activity;
import android.content.Intent;
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
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewProfilePic;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private String profilePicPath = "";

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DatabaseHelper(requireContext());
        sessionManager = SessionManager.getInstance(requireContext());

        imageViewProfilePic = view.findViewById(R.id.imageView_profilePic);
        editTextFirstName = view.findViewById(R.id.editText_profile_firstName);
        editTextLastName = view.findViewById(R.id.editText_profile_lastName);
        editTextPhone = view.findViewById(R.id.editText_profile_phone);
        editTextPassword = view.findViewById(R.id.editText_profile_password);
        editTextConfirmPassword = view.findViewById(R.id.editText_profile_confirmPassword);
        Button buttonChangePic = view.findViewById(R.id.button_changePic);
        Button buttonSave = view.findViewById(R.id.button_saveProfile);

        loadUserInfo();

        buttonChangePic.setOnClickListener(v -> openGallery());
        buttonSave.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void loadUserInfo() {
        int userId = sessionManager.getUserId();
        List<User> allUsers = databaseHelper.getAllUsers();
        for (User user : allUsers) {
            if (user.getId() == userId) {
                editTextFirstName.setText(user.getFirstName());
                editTextLastName.setText(user.getLastName());
                editTextPhone.setText(user.getPhone());
                profilePicPath = user.getProfilePic();

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
        }
    }

    private void saveProfile() {
        if (InputValidator.isEmpty(editTextFirstName)) {
            editTextFirstName.setError("First name is required");
            return;
        }
        if (editTextFirstName.getText().toString().trim().length() < 3) {
            editTextFirstName.setError("First name must be at least 3 characters");
            return;
        }

        if (InputValidator.isEmpty(editTextLastName)) {
            editTextLastName.setError("Last name is required");
            return;
        }
        if (editTextLastName.getText().toString().trim().length() < 3) {
            editTextLastName.setError("Last name must be at least 3 characters");
            return;
        }

        if (InputValidator.isEmpty(editTextPhone)) {
            editTextPhone.setError("Phone is required");
            return;
        }
        if (!InputValidator.isValidPhone(editTextPhone)) {
            editTextPhone.setError("Phone must be at least 10 digits");
            return;
        }

        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String newPassword = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        String passwordToSave = getCurrentPassword();

        if (!newPassword.isEmpty()) {
            if (newPassword.length() < 6) {
                editTextPassword.setError("Password must be at least 6 characters");
                return;
            }
            if (!hasLetterAndNumber(newPassword)) {
                editTextPassword.setError("Password must contain a letter and a number");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                editTextConfirmPassword.setError("Passwords do not match");
                return;
            }
            passwordToSave = PasswordEncryptor.encrypt(newPassword);
        }

        int userId = sessionManager.getUserId();
        databaseHelper.updateUser(userId, firstName, lastName, phone, passwordToSave, profilePicPath);
        Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewProfilePic.setImageBitmap(bitmap);
                profilePicPath = imageUri.toString();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Could not load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getCurrentPassword() {
        int userId = sessionManager.getUserId();
        List<User> allUsers = databaseHelper.getAllUsers();
        for (User user : allUsers) {
            if (user.getId() == userId) {
                return user.getPassword();
            }
        }
        return "";
    }

    private boolean hasLetterAndNumber(String password) {
        boolean hasLetter = false;
        boolean hasNumber = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasNumber = true;
        }
        return hasLetter && hasNumber;
    }
}