package com.example.a1221858_1220137_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class IntroductionActivity extends AppCompatActivity {

    private Button btnConnect;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        btnConnect = findViewById(R.id.btn_connect);
        progressBar = findViewById(R.id.progress_bar);

        btnConnect.setOnClickListener(v -> {
            ConnectionAsyncTask task = new ConnectionAsyncTask(IntroductionActivity.this);
            task.execute("https://6a33f5818248ee962fa4cd44.mockapi.io/trips");
        });
    }

    public void setButtonText(String text) {
        btnConnect.setText(text);
    }

    public void setProgressVisibility(boolean visible) {
        if (progressBar != null) {
            progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void onDataFetchSuccess(String rawJson) {
        List<Trip> fetchedTrips = TripJsonParser.getTripsFromJson(rawJson);
        
        if (fetchedTrips != null && !fetchedTrips.isEmpty()) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            for (Trip trip : fetchedTrips) {
                dbHelper.insertTrip(trip);
            }

            Toast.makeText(this, "Sync Complete! Saved " + fetchedTrips.size() + " trips.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            String message = (fetchedTrips == null) ? "Parsing error: Invalid data format." : "Connected, but no trips were found.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void onDataFetchFailure() {
        Toast.makeText(this, "Network error. Please check connection.", Toast.LENGTH_SHORT).show();
    }
}
