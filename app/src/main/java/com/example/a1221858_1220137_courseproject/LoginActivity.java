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

    private EditText editTextEmail;
    private EditText editTextPassword;
    private CheckBox checkBoxRememberMe;
    private Button buttonLogin;
    private Button buttonSignUp;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = SessionManager.getInstance(this);

        if (sessionManager.isLoggedIn()) {
            goToHome();
            return;
        }

        editTextEmail    = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);
        checkBoxRememberMe = findViewById(R.id.checkBox_rememberMe);
        buttonLogin  = findViewById(R.id.button_login);
        buttonSignUp = findViewById(R.id.button_signUp);

        loadRememberedEmail();

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        buttonLogin.startAnimation(fadeIn);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleLogin() {
        if (InputValidator.isEmpty(editTextEmail)) {
            editTextEmail.setError("Email is required");
            return;
        }
        if (!InputValidator.isValidEmail(editTextEmail)) {
            editTextEmail.setError("Enter a valid email");
            return;
        }
        if (InputValidator.isEmpty(editTextPassword)) {
            editTextPassword.setError("Password is required");
            return;
        }

        String email    = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        String encryptedPassword = PasswordEncryptor.encrypt(password);
        Cursor cursor = databaseHelper.getUserByEmailAndPassword(email, encryptedPassword);

        if (cursor != null && cursor.moveToFirst()) {
            int userId      = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("FIRST_NAME"));
            int isAdmin     = cursor.getInt(cursor.getColumnIndexOrThrow("IS_ADMIN"));
            cursor.close();

            sessionManager.saveLoginSession(userId, email, firstName, isAdmin);

            if (checkBoxRememberMe.isChecked()) {
                sessionManager.saveRememberedEmail(email);
            } else {
                sessionManager.clearRememberedEmail();
            }

            Toast.makeText(this, "Welcome, " + firstName + "!", Toast.LENGTH_SHORT).show();
            goToHome();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRememberedEmail() {
        String savedEmail = sessionManager.getRememberedEmail();
        if (!savedEmail.isEmpty()) {
            editTextEmail.setText(savedEmail);
            checkBoxRememberMe.setChecked(true);
        }
    }

    private void goToHome() {
        Intent intent;
        if (sessionManager.isAdmin()) {
            intent = new Intent(LoginActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
