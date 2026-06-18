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

public class ReservationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewNoReservations;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    public ReservationsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);
        databaseHelper = new DatabaseHelper(requireContext());
        sessionManager = SessionManager.getInstance(requireContext());
        recyclerView = view.findViewById(R.id.recyclerView_reservations);
        textViewNoReservations = view.findViewById(R.id.textView_noReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadReservations();
        return view;
    }

    private void loadReservations() {
        int userId = sessionManager.getUserId();
        Cursor cursor = databaseHelper.getReservationsByUserId(userId);
        List<Reservation> reservationList = new ArrayList<>();
        List<String> tripNameList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation();
                reservation.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
                reservation.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID")));
                reservation.setTripId(cursor.getInt(cursor.getColumnIndexOrThrow("TRIP_ID")));
                reservation.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY")));
                reservation.setType(cursor.getString(cursor.getColumnIndexOrThrow("TYPE")));
                reservation.setDate(cursor.getString(cursor.getColumnIndexOrThrow("DATE")));
                reservation.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("STATUS")));
                reservationList.add(reservation);
                tripNameList.add(getTripName(reservation.getTripId()));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (reservationList.isEmpty()) {
            textViewNoReservations.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNoReservations.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            ReservationAdapter adapter = new ReservationAdapter(requireContext(), reservationList, tripNameList);
            recyclerView.setAdapter(adapter);
        }
    }

    private String getTripName(int tripId) {
        List<Trip> allTrips = databaseHelper.getAllTrips();
        for (Trip trip : allTrips) {
            if (trip.getId() == tripId) {
                return trip.getDestination();
            }
        }
        return "Unknown Trip";
    }
}