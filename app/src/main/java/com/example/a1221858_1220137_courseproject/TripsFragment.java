package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a1221858_1220137_courseproject.R;
import com.example.a1221858_1220137_courseproject.TripAdapter;
import com.example.a1221858_1220137_courseproject.DataBaseHelper;
import com.example.a1221858_1220137_courseproject.Trip;
import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private EditText etSearch, etMaxPrice;
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private DataBaseHelper dbHelper;
    private List<Trip> allTripsList = new ArrayList<>();
    private List<Trip> filteredTripsList = new ArrayList<>();

    public TripsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        etSearch = view.findViewById(R.id.et_search_destination);
        etMaxPrice = view.findViewById(R.id.et_filter_price);
        recyclerView = view.findViewById(R.id.rv_trips_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());
        allTripsList = dbHelper.getAllTrips();
        filteredTripsList.addAll(allTripsList);

        adapter = new TripAdapter(filteredTripsList, this);
        recyclerView.setAdapter(adapter);

        TextWatcher filterWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etSearch.addTextChangedListener(filterWatcher);
        etMaxPrice.addTextChangedListener(filterWatcher);

        return view;
    }

    private void applyFilters() {
        String searchQuery = etSearch.getText().toString().trim().toLowerCase();
        String priceQuery = etMaxPrice.getText().toString().trim();

        double maxBudget = Double.MAX_VALUE;
        if (!priceQuery.isEmpty()) {
            try {
                maxBudget = Double.parseDouble(priceQuery);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        filteredTripsList.clear();
        for (Trip trip : allTripsList) {
            boolean matchesSearch = trip.getDestination().toLowerCase().contains(searchQuery);
            boolean matchesPrice = trip.getPrice() <= maxBudget;

            if (matchesSearch && matchesPrice) {
                filteredTripsList.add(trip);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTripClick(Trip trip) {
        TripDetailFragment detailFragment = TripDetailFragment.newInstance(trip.getId());
        if (getFragmentManager() != null) {
            androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}