package com.example.a1221858_1220137_courseproject;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoriteAdapter.FavoriteActionListener {

    private RecyclerView recyclerView;
    private TextView textViewNoFavorites;
    private FavoriteAdapter favoriteAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private List<Trip> favoriteTrips;

    public FavoritesFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        databaseHelper = new DatabaseHelper(requireContext());
        sessionManager = SessionManager.getInstance(requireContext());
        recyclerView = view.findViewById(R.id.recyclerView_favorites);
        textViewNoFavorites = view.findViewById(R.id.textView_noFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadFavorites();
        return view;
    }

    private void loadFavorites() {
        int userId = sessionManager.getUserId();
        favoriteTrips = new ArrayList<>();
        Cursor favCursor = databaseHelper.getFavoritesByUserId(userId);

        if (favCursor != null && favCursor.moveToFirst()) {
            do {
                int tripId = favCursor.getInt(favCursor.getColumnIndexOrThrow("TRIP_ID"));
                Trip trip = getTripById(tripId);
                if (trip != null) {
                    favoriteTrips.add(trip);
                }
            } while (favCursor.moveToNext());
            favCursor.close();
        }

        if (favoriteTrips.isEmpty()) {
            textViewNoFavorites.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNoFavorites.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            favoriteAdapter = new FavoriteAdapter(requireContext(), favoriteTrips, this);
            recyclerView.setAdapter(favoriteAdapter);
        }
    }

    private Trip getTripById(int tripId) {
        List<Trip> allTrips = databaseHelper.getAllTrips();
        for (Trip trip : allTrips) {
            if (trip.getId() == tripId) {
                return trip;
            }
        }
        return null;
    }

    @Override
    public void onRemoveFavorite(Trip trip, int position) {
        int userId = sessionManager.getUserId();
        databaseHelper.removeFavorite(userId, trip.getId());
        favoriteAdapter.removeItem(position);
        Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
        if (favoriteTrips.isEmpty()) {
            textViewNoFavorites.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBookTrip(Trip trip) {
        ReservationDialogHelper dialogHelper = new ReservationDialogHelper(requireContext());
        dialogHelper.showReservationDialog(trip.getId(), trip.getDestination());
    }
}