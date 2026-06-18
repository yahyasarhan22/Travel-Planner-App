package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.a1221858_1220137_courseproject.R;
import com.example.a1221858_1220137_courseproject.DatabaseHelper;
import com.example.a1221858_1220137_courseproject.Trip;

public class TripDetailFragment extends Fragment {

    private static final String ARG_TRIP_ID = "trip_id";
    private int tripId;
    private DatabaseHelper dbHelper;

    public TripDetailFragment() {
    }

    public static TripDetailFragment newInstance(int tripId) {
        TripDetailFragment fragment = new TripDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRIP_ID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tripId = getArguments().getInt(ARG_TRIP_ID);
        }
        dbHelper = new DatabaseHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_detail, container, false);

        TextView tvDestination = view.findViewById(R.id.detail_tv_destination);
        TextView tvCountry = view.findViewById(R.id.detail_tv_country);
        TextView tvDuration = view.findViewById(R.id.detail_tv_duration);
        TextView tvPrice = view.findViewById(R.id.detail_tv_price);
        TextView tvRating = view.findViewById(R.id.detail_tv_rating);
        TextView tvDescription = view.findViewById(R.id.detail_tv_description);
        Button btnBook = view.findViewById(R.id.btn_book_trip);

        Trip currentTrip = null;
        for (Trip t : dbHelper.getAllTrips()) {
            if (t.getId() == tripId) {
                currentTrip = t;
                break;
            }
        }

        if (currentTrip != null) {
            tvDestination.setText(currentTrip.getDestination());
            tvCountry.setText(currentTrip.getCountry());
            tvDuration.setText(currentTrip.getDurationDays() + " Days");
            tvPrice.setText("$" + currentTrip.getPrice());
            tvRating.setText("★ " + currentTrip.getRating());
            tvDescription.setText(currentTrip.getDescription());
        }

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Opening reservation form...", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}