package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a1221858_1220137_courseproject.R;
import com.example.a1221858_1220137_courseproject.TripAdapter;
import com.example.a1221858_1220137_courseproject.DatabaseHelper;
import com.example.a1221858_1220137_courseproject.Trip;
import java.util.ArrayList;
import java.util.List;

public class SpecialFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Trip> popularTripsList = new ArrayList<>();

    public SpecialFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special, container, false);

        recyclerView = view.findViewById(R.id.rv_special_trips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DatabaseHelper(getContext());

        popularTripsList.clear();
        for (Trip trip : dbHelper.getAllTrips()) {
            if (trip.getRating() >= 4.5) {
                popularTripsList.add(trip);
            }
        }

        adapter = new TripAdapter(popularTripsList, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onTripClick(Trip trip) {
        TripDetailFragment detailFragment = TripDetailFragment.newInstance(trip.getId());
        if (getFragmentManager() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}