package com.example.a1221858_1220137_courseproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // UI elements
    private EditText editTextEmail;
    private EditText editTextPassword;
    private CheckBox checkBoxRememberMe;
    private Button buttonLogin;
    private Button buttonSignUp;

    // Helper classes
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up helpers
        databaseHelper = new DatabaseHelper(this, "TripApp.db", null, 1);
        sessionManager = SessionManager.getInstance(this);

        // If user is already logged in, skip login screen
        if (sessionManager.isLoggedIn()) {
            goToHome();
            return;
        }

        // Connect UI elements to their IDs in the layout
        editTextEmail    = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);
        checkBoxRememberMe = findViewById(R.id.checkBox_rememberMe);
        buttonLogin  = findViewById(R.id.button_login);
        buttonSignUp = findViewById(R.id.button_signUp);

        // Load saved email if Remember Me was checked before
        loadRememberedEmail();

        // Play fade-in animation on login button (uses partner's fade_in.xml)
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        buttonLogin.startAnimation(fadeIn);

        // Login button click
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Sign up button click — go to register screen
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Check inputs, then try to login the user
    private void handleLogin() {

        // Validate email is not empty
        if (InputValidator.isEmpty(editTextEmail)) {
            editTextEmail.setError("Email is required");
            return;
        }

        // Validate email format
        if (!InputValidator.isValidEmail(editTextEmail)) {
            editTextEmail.setError("Enter a valid email");
            return;
        }

        // Validate password is not empty
        if (InputValidator.isEmpty(editTextPassword)) {
            editTextPassword.setError("Password is required");
            return;
        }

        String email    = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Encrypt the password before checking (MD5 — simple encryption)
        String encryptedPassword = PasswordEncryptor.encrypt(password);

        // Check database for matching user
        Cursor cursor = databaseHelper.getUserByEmailAndPassword(email, encryptedPassword);

        if (cursor != null && cursor.moveToFirst()) {
            // Login success — read user info from cursor
            int userId      = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("FIRST_NAME"));
            int isAdmin     = cursor.getInt(cursor.getColumnIndexOrThrow("IS_ADMIN"));
            cursor.close();

            // Save session so user stays logged in
            sessionManager.saveLoginSession(userId, email, firstName, isAdmin);

            // Save email if Remember Me is checked
            if (checkBoxRememberMe.isChecked()) {
                sessionManager.saveRememberedEmail(email);
            } else {
                sessionManager.clearRememberedEmail();
            }

            Toast.makeText(this, "Welcome, " + firstName + "!", Toast.LENGTH_SHORT).show();

            // Go to home screen
            goToHome();

        } else {
            // Login failed
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    // Load the saved email into the email field if it was remembered
    private void loadRememberedEmail() {
        String savedEmail = sessionManager.getRememberedEmail();
        if (!savedEmail.isEmpty()) {
            editTextEmail.setText(savedEmail);
            checkBoxRememberMe.setChecked(true);
        }
    }

    // Navigate to home — admin gets admin home, regular user gets main home
    private void goToHome() {
        Intent intent;
        if (sessionManager.isAdmin()) {
            intent = new Intent(LoginActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish(); // close login screen so back button won't return here
    }
}