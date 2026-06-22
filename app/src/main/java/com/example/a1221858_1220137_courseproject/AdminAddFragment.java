package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;

public class AdminAddFragment extends Fragment {

    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextPhone;
    private Spinner spinnerGender;
    private Spinner spinnerMajor;
    private Button buttonRegister;

    private DatabaseHelper databaseHelper;

    public AdminAddFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_add, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        editTextEmail           = view.findViewById(R.id.editText_admin_email);
        editTextFirstName       = view.findViewById(R.id.editText_admin_firstName);
        editTextLastName        = view.findViewById(R.id.editText_admin_lastName);
        editTextPassword        = view.findViewById(R.id.editText_admin_password);
        editTextConfirmPassword = view.findViewById(R.id.editText_admin_confirmPassword);
        editTextPhone           = view.findViewById(R.id.editText_admin_phone);
        spinnerGender           = view.findViewById(R.id.spinner_admin_gender);
        spinnerMajor            = view.findViewById(R.id.spinner_admin_major);
        buttonRegister          = view.findViewById(R.id.button_admin_register);

        setupSpinners();

        if (getContext() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            buttonRegister.startAnimation(fadeIn);
        }

        buttonRegister.setOnClickListener(v -> handleAdminRegister());

        return view;
    }

    private void setupSpinners() {
        if (getContext() == null) return;

        String[] genderOptions = {"Select Gender", "Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_item,
                genderOptions
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

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
                getContext(),
                R.layout.spinner_item,
                majorOptions
        );
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(majorAdapter);
    }

    private void handleAdminRegister() {
        if (InputValidator.isEmpty(editTextEmail)) {
            editTextEmail.setError("Email is required");
            return;
        }
        if (!InputValidator.isValidEmail(editTextEmail)) {
            editTextEmail.setError("Enter a valid email");
            return;
        }

        String email = editTextEmail.getText().toString().trim();
        List<User> userList = databaseHelper.getAllUsers();
        for (User account : userList) {
            if (account.getEmail().equalsIgnoreCase(email)) {
                editTextEmail.setError("This email address is already registered");
                Toast.makeText(getContext(), "Account already exists!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

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

        if (InputValidator.isEmpty(editTextPassword)) {
            editTextPassword.setError("Password is required");
            return;
        }
        if (!isPasswordValid(editTextPassword.getText().toString().trim())) {
            editTextPassword.setError("Password must be 6+ chars, include a letter and a number");
            return;
        }

        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        if (spinnerGender.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerMajor.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select a major", Toast.LENGTH_SHORT).show();
            return;
        }

        if (InputValidator.isEmpty(editTextPhone)) {
            editTextPhone.setError("Phone number is required");
            return;
        }
        if (!InputValidator.isValidPhone(editTextPhone)) {
            editTextPhone.setError("Phone must be at least 10 digits");
            return;
        }

        String firstName = editTextFirstName.getText().toString().trim();
        String lastName  = editTextLastName.getText().toString().trim();
        String gender    = spinnerGender.getSelectedItem().toString();
        String major     = spinnerMajor.getSelectedItem().toString();
        String phone     = editTextPhone.getText().toString().trim();

        String encryptedPassword = PasswordEncryptor.encrypt(password);

        User newAdmin = new User(
                0,
                email,
                firstName,
                lastName,
                encryptedPassword,
                gender,
                major,
                phone,
                "",
                1
        );

        databaseHelper.insertUser(newAdmin);
        Toast.makeText(getContext(), "Admin account added successfully!", Toast.LENGTH_LONG).show();
        clearFields();
    }

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

    private void clearFields() {
        editTextEmail.setText("");
        editTextFirstName.setText("");
        editTextLastName.setText("");
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");
        editTextPhone.setText("");
        spinnerGender.setSelection(0);
        spinnerMajor.setSelection(0);
    }
}
