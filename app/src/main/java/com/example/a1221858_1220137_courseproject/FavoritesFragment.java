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

// FavoritesFragment shows all trips the user has marked as favorite
// Implements FavoriteActionListener to handle remove and book actions
public class FavoritesFragment extends Fragment implements FavoriteAdapter.FavoriteActionListener {

    private RecyclerView recyclerView;
    private TextView textViewNoFavorites;
    private FavoriteAdapter favoriteAdapter;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    private List<Trip> favoriteTrips;

    // Required empty constructor for fragments
    public FavoritesFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Set up helpers
        databaseHelper = new DatabaseHelper(requireContext(), "TripApp.db", null, 1);
        sessionManager = SessionManager.getInstance(requireContext());

        // Connect views
        recyclerView       = view.findViewById(R.id.recyclerView_favorites);
        textViewNoFavorites = view.findViewById(R.id.textView_noFavorites);

        // Set vertical list layout for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Load and show the favorites
        loadFavorites();

        return view;
    }

    // Read favorites from DB and show them
    private void loadFavorites() {
        int userId = sessionManager.getUserId();
        favoriteTrips = new ArrayList<>();

        // Get all favorite trip IDs for this user
        Cursor favCursor = databaseHelper.getFavoritesByUserId(userId);

        if (favCursor != null && favCursor.moveToFirst()) {
            do {
                int tripId = favCursor.getInt(favCursor.getColumnIndexOrThrow("TRIP_ID"));

                // Get the full trip details using tripId
                Trip trip = getTripById(tripId);
                if (trip != null) {
                    favoriteTrips.add(trip);
                }
            } while (favCursor.moveToNext());
            favCursor.close();
        }

        // Show empty message if no favorites
        if (favoriteTrips.isEmpty()) {
            textViewNoFavorites.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNoFavorites.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Set adapter — pass this fragment as the listener
            favoriteAdapter = new FavoriteAdapter(requireContext(), favoriteTrips, this);
            recyclerView.setAdapter(favoriteAdapter);
        }
    }

    // Find a trip by its ID from the database
    private Trip getTripById(int tripId) {
        Cursor cursor = databaseHelper.getAllTrips();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                if (id == tripId) {
                    Trip trip = new Trip();
                    trip.setId(id);
                    trip.setDestination(cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION")));
                    trip.setDuration(cursor.getString(cursor.getColumnIndexOrThrow("DURATION")));
                    trip.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE")));
                    trip.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")));
                    cursor.close();
                    return trip;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return null;
    }

    // Called when user clicks Remove on a favorite card
    @Override
    public void onRemoveFavorite(Trip trip, int position) {
        int userId = sessionManager.getUserId();

        // Remove from database
        databaseHelper.removeFavorite(userId, trip.getId());

        // Remove from the list and refresh the RecyclerView
        favoriteAdapter.removeItem(position);

        Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();

        // Show empty message if list is now empty
        if (favoriteTrips.isEmpty()) {
            textViewNoFavorites.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    // Called when user clicks Book Now on a favorite card
    @Override
    public void onBookTrip(Trip trip) {
        // Show the reservation dialog for this trip
        ReservationDialogHelper dialogHelper = new ReservationDialogHelper(requireContext());
        dialogHelper.showReservationDialog(trip.getId(), trip.getDestination());
    }
}