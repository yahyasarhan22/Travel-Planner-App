package com.example.a1221858_1220137_courseproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Button btnTrips, btnUsers, btnReservations, btnAddAdmin, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnTrips = findViewById(R.id.btn_nav_trips);
        btnUsers = findViewById(R.id.btn_nav_users);
        btnReservations = findViewById(R.id.btn_nav_reservations);
        btnAddAdmin = findViewById(R.id.btn_nav_add_admin);
        btnLogout = findViewById(R.id.btn_admin_logout);

        switchPanel(new AdminTripsFragment());

        btnTrips.setOnClickListener(v -> {
            updateNavColors(btnTrips);
            switchPanel(new AdminTripsFragment());
        });

        btnUsers.setOnClickListener(v -> {
            updateNavColors(btnUsers);
            switchPanel(new AdminUsersFragment());
        });

        btnReservations.setOnClickListener(v -> {
            updateNavColors(btnReservations);
            switchPanel(new AdminReservationsFragment());
        });

        btnAddAdmin.setOnClickListener(v -> {
            updateNavColors(btnAddAdmin);
            switchPanel(new AdminAddFragment());
        });

        btnLogout.setOnClickListener(v -> {
            LogoutHelper.showLogoutDialog(AdminActivity.this);
        });
    }

    private void updateNavColors(Button activeSelection) {
        btnTrips.setTextColor(getResources().getColor(R.color.gray_text));
        btnUsers.setTextColor(getResources().getColor(R.color.gray_text));
        btnReservations.setTextColor(getResources().getColor(R.color.gray_text));
        btnAddAdmin.setTextColor(getResources().getColor(R.color.gray_text)); // Reset selection gray

        activeSelection.setTextColor(getResources().getColor(R.color.left_primary));
    }

    private void switchPanel(Fragment targetFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.admin_fragment_container, targetFragment)
                .commit();
    }
}