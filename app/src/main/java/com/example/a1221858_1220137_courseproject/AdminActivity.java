package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Button btnTrips, btnUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnTrips = findViewById(R.id.btn_nav_trips);
        btnUsers = findViewById(R.id.btn_nav_users);

        switchPanel(new AdminTripsFragment());

        btnTrips.setOnClickListener(v -> {
            btnTrips.setTextColor(getResources().getColor(R.color.left_primary));
            btnUsers.setTextColor(getResources().getColor(R.color.gray_text));
            switchPanel(new AdminTripsFragment());
        });

        btnUsers.setOnClickListener(v -> {
            btnUsers.setTextColor(getResources().getColor(R.color.left_primary));
            btnTrips.setTextColor(getResources().getColor(R.color.gray_text));
            switchPanel(new AdminUsersFragment());
        });
    }

    private void switchPanel(Fragment targetFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.admin_fragment_container, targetFragment)
                .commit();
    }
}