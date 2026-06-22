package com.example.a1221858_1220137_courseproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactUsFragment extends Fragment {

    public ContactUsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        Animation bounceAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        Animation entryAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        view.startAnimation(entryAnimation);

        Button buttonCall = view.findViewById(R.id.button_callUs);
        Button buttonLocate = view.findViewById(R.id.button_locateUs);
        Button buttonEmail = view.findViewById(R.id.button_emailUs);

        buttonCall.setOnClickListener(v -> {
            v.startAnimation(bounceAnim);
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:+970569512402"));
            startActivity(callIntent);
        });

        buttonLocate.setOnClickListener(v -> {
            v.startAnimation(bounceAnim);
            Uri mapUri = Uri.parse("geo:31.9740,35.2316?q=Birzeit+University");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        buttonEmail.setOnClickListener(v -> {
            v.startAnimation(bounceAnim);
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:travelplanner@support.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Travel Planner App Inquiry");
            startActivity(emailIntent);
        });

        return view;
    }
}
