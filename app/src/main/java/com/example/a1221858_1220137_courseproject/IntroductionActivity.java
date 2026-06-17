package com.example.a1221858_1220137_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a1221858_1220137_courseproject.R;
import com.example.a1221858_1220137_courseproject.Trip;
import com.example.a1221858_1220137_courseproject.ConnectionAsyncTask;
import com.example.a1221858_1220137_courseproject.TripJsonParser;
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

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsyncTask task = new ConnectionAsyncTask(IntroductionActivity.this);
                task.execute("http://your-rest-api-endpoint/api/trips");
            }
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

            for (Trip trip : fetchedTrips) {
            }

            Toast.makeText(this, "Sync Complete! Saved " + fetchedTrips.size() + " trips.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to parse synchronized payloads.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDataFetchFailure() {
        Toast.makeText(this, "Network error. Please inspect connection configuration.", Toast.LENGTH_SHORT).show();
    }
}