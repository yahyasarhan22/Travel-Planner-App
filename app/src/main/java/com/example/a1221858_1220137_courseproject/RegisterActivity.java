package com.example.a1221858_1220137_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // UI elements
    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextPhone;
    private Spinner spinnerGender;
    private Spinner spinnerMajor;
    private Button buttonRegister;
    private Button buttonBackToLogin;

    // Database helper
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up database helper
        databaseHelper = new DatabaseHelper(this, "TripApp.db", null, 1);

        // Connect all UI elements to their layout IDs
        editTextEmail           = findViewById(R.id.editText_reg_email);
        editTextFirstName       = findViewById(R.id.editText_firstName);
        editTextLastName        = findViewById(R.id.editText_lastName);
        editTextPassword        = findViewById(R.id.editText_reg_password);
        editTextConfirmPassword = findViewById(R.id.editText_confirmPassword);
        editTextPhone           = findViewById(R.id.editText_phone);
        spinnerGender           = findViewById(R.id.spinner_gender);
        spinnerMajor            = findViewById(R.id.spinner_major);
        buttonRegister          = findViewById(R.id.button_register);
        buttonBackToLogin       = findViewById(R.id.button_backToLogin);

        // Fill the spinners with options
        setupSpinners();

        // Play fade-in animation on the register button
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        buttonRegister.startAnimation(fadeIn);

        // Register button click
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        // Back to login button click
        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // just close this screen, login is already behind it
            }
        });
    }

    // Fill gender and major spinners with options using ArrayAdapter
    // Same approach as lab manual spinner setup
    private void setupSpinners() {

        // Gender options using the custom high-visibility item layout
        String[] genderOptions = {"Select Gender", "Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item, // Swapped from android.R.layout.simple_spinner_item
                genderOptions
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Major options using the custom high-visibility item layout
        String[] majorOptions = {
                "Select Major",
                "Computer Engineering",
                "Electrical Engineering",
                "Civil Engineering",
                "Mechanical Engineering",
                "Business Administration",
                "Computer Science",
                "Other"
        };
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item, // Swapped from android.R.layout.simple_spinner_item
                majorOptions
        );
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(majorAdapter);
    }

    // Validate all inputs then save the new user to the database
    private void handleRegister() {

        // --- Validate Email ---
        if (InputValidator.isEmpty(editTextEmail)) {
            editTextEmail.setError("Email is required");
            return;
        }
        if (!InputValidator.isValidEmail(editTextEmail)) {
            editTextEmail.setError("Enter a valid email");
            return;
        }

        // --- Validate First Name (min 3 characters) ---
        if (InputValidator.isEmpty(editTextFirstName)) {
            editTextFirstName.setError("First name is required");
            return;
        }
        if (editTextFirstName.getText().toString().trim().length() < 3) {
            editTextFirstName.setError("First name must be at least 3 characters");
            return;
        }

        // --- Validate Last Name (min 3 characters) ---
        if (InputValidator.isEmpty(editTextLastName)) {
            editTextLastName.setError("Last name is required");
            return;
        }
        if (editTextLastName.getText().toString().trim().length() < 3) {
            editTextLastName.setError("Last name must be at least 3 characters");
            return;
        }

        // --- Validate Password ---
        if (InputValidator.isEmpty(editTextPassword)) {
            editTextPassword.setError("Password is required");
            return;
        }
        if (!isPasswordValid(editTextPassword.getText().toString().trim())) {
            editTextPassword.setError("Password must be 6+ chars, include a letter and a number");
            return;
        }

        // --- Validate Confirm Password matches Password ---
        String password        = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        // --- Validate Gender spinner (not default option) ---
        if (spinnerGender.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Validate Major spinner (not default option) ---
        if (spinnerMajor.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a major", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Validate Phone ---
        if (InputValidator.isEmpty(editTextPhone)) {
            editTextPhone.setError("Phone number is required");
            return;
        }
        if (!InputValidator.isValidPhone(editTextPhone)) {
            editTextPhone.setError("Phone must be at least 10 digits");
            return;
        }

        // --- All inputs are valid, build the User object ---
        String email     = editTextEmail.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName  = editTextLastName.getText().toString().trim();
        String gender    = spinnerGender.getSelectedItem().toString();
        String major     = spinnerMajor.getSelectedItem().toString();
        String phone     = editTextPhone.getText().toString().trim();

        // Encrypt the password before saving — project requires AES encryption
        String encryptedPassword = PasswordEncryptor.encrypt(password);

        // Create a new User object (id=0, isAdmin=0 for regular users)
        User newUser = new User(
                0,
                email,
                firstName,
                lastName,
                encryptedPassword,
                gender,
                major,
                phone,
                "",   // no profile picture yet
                0     // not an admin
        );

        // Save the user to the database
        databaseHelper.insertUser(newUser);

        // Show success message
        Toast.makeText(this, "Account created! Please login.", Toast.LENGTH_LONG).show();

        // Go back to login screen
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Check password rules from project spec:
    // at least 6 chars, at least 1 letter, at least 1 number
    private boolean isPasswordValid(String password) {
        if (password.length() < 6) return false;

        boolean hasLetter = false;
        boolean hasNumber = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c))  hasNumber = true;
        }

        return hasLetter && hasNumber;
    }
}
