package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView; // Added import
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide; // Added import
import com.example.a1221858_1220137_courseproject.R;
import com.example.a1221858_1220137_courseproject.DatabaseHelper;
import com.example.a1221858_1220137_courseproject.Trip;
import java.util.List;

public class TripDetailFragment extends Fragment {

    private static final String ARG_TRIP_ID = "trip_id";
    private int tripId;
    private DatabaseHelper dbHelper;

    public TripDetailFragment() {}

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

        // Bind layout elements
        ImageView ivTripImage = view.findViewById(R.id.detail_iv_trip);
        TextView tvDestination = view.findViewById(R.id.detail_tv_destination);
        TextView tvCountry = view.findViewById(R.id.detail_tv_country);
        TextView tvDuration = view.findViewById(R.id.detail_tv_duration);
        TextView tvPrice = view.findViewById(R.id.detail_tv_price);
        TextView tvRating = view.findViewById(R.id.detail_tv_rating);
        TextView tvDescription = view.findViewById(R.id.detail_tv_description);
        Button btnBook = view.findViewById(R.id.btn_book_trip);

        Trip foundTrip = null;
        List<Trip> trips = dbHelper.getAllTrips();
        for (Trip t : trips) {
            if (t.getId() == tripId) {
                foundTrip = t;
                break;
            }
        }

        final Trip currentTrip = foundTrip;

        if (currentTrip != null) {
            tvDestination.setText(currentTrip.getDestination());
            tvCountry.setText(currentTrip.getCountry());
            tvDuration.setText(currentTrip.getDuration());
            tvPrice.setText("$" + currentTrip.getPrice());
            tvRating.setText("★ " + currentTrip.getRating());
            tvDescription.setText(currentTrip.getDescription());

            Glide.with(this)
                    .load(currentTrip.getImageUrl())
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(ivTripImage);
        }

        btnBook.setOnClickListener(v -> {
            if (currentTrip != null) {
                ReservationDialogHelper dialogHelper = new ReservationDialogHelper(requireContext());
                dialogHelper.showReservationDialog(currentTrip.getId(), currentTrip.getDestination());
            }
        });

        return view;
    }
}