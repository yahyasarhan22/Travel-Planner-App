package com.example.a1221858_1220137_courseproject;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

// ReservationsFragment shows all reservations made by the logged-in user
public class ReservationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewNoReservations;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    // Required empty constructor for fragments
    public ReservationsFragment() {}

    // onCreateView inflates the fragment layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);

        // Set up helpers
        databaseHelper = new DatabaseHelper(requireContext(), "TripApp.db", null, 1);
        sessionManager = SessionManager.getInstance(requireContext());

        // Connect views
        recyclerView          = view.findViewById(R.id.recyclerView_reservations);
        textViewNoReservations = view.findViewById(R.id.textView_noReservations);

        // Set RecyclerView to show items in a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Load and show reservations for the current user
        loadReservations();

        return view;
    }

    // Read reservations from DB and pass them to the adapter
    private void loadReservations() {
        int userId = sessionManager.getUserId();

        // Get all reservations for this user from the database
        Cursor cursor = databaseHelper.getReservationsByUserId(userId);

        List<Reservation> reservationList = new ArrayList<>();
        List<String> tripNameList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Read each reservation from the cursor
                Reservation reservation = new Reservation();
                reservation.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
                reservation.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID")));
                reservation.setTripId(cursor.getInt(cursor.getColumnIndexOrThrow("TRIP_ID")));
                reservation.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY")));
                reservation.setType(cursor.getString(cursor.getColumnIndexOrThrow("TYPE")));
                reservation.setDate(cursor.getString(cursor.getColumnIndexOrThrow("DATE")));
                reservation.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("STATUS")));

                reservationList.add(reservation);

                // Get the trip name using the tripId
                String tripName = getTripName(reservation.getTripId());
                tripNameList.add(tripName);

            } while (cursor.moveToNext());

            cursor.close();
        }

        // Show empty message if no reservations found
        if (reservationList.isEmpty()) {
            textViewNoReservations.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNoReservations.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Set the adapter on the RecyclerView
            ReservationAdapter adapter = new ReservationAdapter(
                    requireContext(),
                    reservationList,
                    tripNameList
            );
            recyclerView.setAdapter(adapter);
        }
    }

    // Get the destination name of a trip by its ID from the database
    private String getTripName(int tripId) {
        Cursor cursor = databaseHelper.getAllTrips();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                if (id == tripId) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION"));
                    cursor.close();
                    return name;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return "Unknown Trip";
    }
}