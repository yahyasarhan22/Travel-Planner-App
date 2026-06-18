package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SpecialFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Trip> popularTripsList = new ArrayList<>();

    public SpecialFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special, container, false);

        // Initialize RecyclerView and Layout Manager
        recyclerView = view.findViewById(R.id.rv_special_trips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the class-level database helper
        dbHelper = new DatabaseHelper(getContext());

        // Load filtered datasets
        loadPopularTrips();

        // Fetch active user context from your session cache layer
        int currentUserId = SessionManager.getInstance(getContext()).getUserId();

        // Pass dependencies neatly into your custom TripAdapter
        adapter = new TripAdapter(popularTripsList, dbHelper, currentUserId, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadPopularTrips() {
        popularTripsList.clear();
        List<Trip> allTrips = dbHelper.getAllTrips();

        for (Trip trip : allTrips) {
            // Evaluates both rating constraints and budget ceilings simultaneously
            if (trip.getRating() >= 8.5 && trip.getPrice() < 600.0) {
                popularTripsList.add(trip);
            }
        }
    }

    @Override
    public void onTripClick(Trip trip) {
        TripDetailFragment detailFragment = TripDetailFragment.newInstance(trip.getId());
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}