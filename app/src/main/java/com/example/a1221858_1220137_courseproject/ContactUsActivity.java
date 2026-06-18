package com.example.a1221858_1220137_courseproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

// ContactUsActivity has 3 buttons — call, maps, email
// Each uses an Intent to open another app (phone dialer, maps, email)
public class ContactUsActivity extends AppCompatActivity {

    private Button buttonCall;
    private Button buttonLocate;
    private Button buttonEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        // Connect views
        buttonCall   = findViewById(R.id.button_callUs);
        buttonLocate = findViewById(R.id.button_locateUs);
        buttonEmail  = findViewById(R.id.button_emailUs);

        // Call Us — opens the phone dialer with a predefined number
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+970591234567"));
                startActivity(callIntent);
            }
        });

        // Locate Us — opens Google Maps with a predefined location
        buttonLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Birzeit University coordinates as example
                Uri mapUri = Uri.parse("geo:31.9740,35.2316?q=Birzeit+University");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        // Email Us — opens Gmail with predefined email address
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:travelplanner@support.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Travel Planner App Inquiry");
                startActivity(emailIntent);
            }
        });
    }
}