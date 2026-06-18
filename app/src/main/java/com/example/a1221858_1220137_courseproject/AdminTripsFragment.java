package com.example.a1221858_1220137_courseproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a1221858_1220137_courseproject.R;
import com.example.a1221858_1220137_courseproject.DatabaseHelper;
import com.example.a1221858_1220137_courseproject.Trip;
import java.util.ArrayList;
import java.util.List;

public class AdminTripsFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button btnAddTrip;
    private DatabaseHelper dbHelper;
    private List<Trip> tripsList = new ArrayList<>();
    private AdminTripAdapter adapter;

    public AdminTripsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_trips, container, false);

        recyclerView = view.findViewById(R.id.rv_admin_trips);
        btnAddTrip = view.findViewById(R.id.btn_admin_add_trip);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new DatabaseHelper(getContext());

        refreshData();

        btnAddTrip.setOnClickListener(v -> showTripDialog(null));

        return view;
    }

    private void refreshData() {
        tripsList.clear();
        tripsList.addAll(dbHelper.getAllTrips());
        if (adapter == null) {
            adapter = new AdminTripAdapter();
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void showTripDialog(@Nullable Trip tripToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_trip_form, null);
        builder.setView(dialogView);

        TextView tvTitle = dialogView.findViewById(R.id.dialog_form_title);
        EditText etDest = dialogView.findViewById(R.id.dialog_et_destination);
        EditText etCountry = dialogView.findViewById(R.id.dialog_et_country);
        EditText etDuration = dialogView.findViewById(R.id.dialog_et_duration);
        EditText etPrice = dialogView.findViewById(R.id.dialog_et_price);
        EditText etDesc = dialogView.findViewById(R.id.dialog_et_description);
        Button btnCancel = dialogView.findViewById(R.id.dialog_btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.dialog_btn_save);

        AlertDialog dialog = builder.create();

        if (tripToEdit != null) {
            tvTitle.setText("Modify Destination");
            etDest.setText(tripToEdit.getDestination());
            etCountry.setText(tripToEdit.getCountry());
            etDuration.setText(String.valueOf(tripToEdit.getDurationDays()));
            etPrice.setText(String.valueOf(tripToEdit.getPrice()));
            etDesc.setText(tripToEdit.getDescription());
        } else {
            tvTitle.setText("Create Destination");
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String dest = etDest.getText().toString().trim();
            String country = etCountry.getText().toString().trim();
            String durStr = etDuration.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (dest.isEmpty() || country.isEmpty() || durStr.isEmpty() || priceStr.isEmpty() || desc.isEmpty()) {
                Toast.makeText(getContext(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Trip trip = (tripToEdit != null) ? tripToEdit : new Trip();
            trip.setDestination(dest);
            trip.setCountry(country);
            trip.setDurationDays(Integer.parseInt(durStr));
            trip.setPrice(Double.parseDouble(priceStr));
            trip.setDescription(desc);

            if (tripToEdit != null) {
                dbHelper.updateTrip(trip);
                Toast.makeText(getContext(), "Destination updated", Toast.LENGTH_SHORT).show();
            } else {
                trip.setId((int) (System.currentTimeMillis() & 0xfffffff));
                dbHelper.insertTrip(trip);
                Toast.makeText(getContext(), "Destination added successfully", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
            refreshData();
        });

        dialog.show();
    }

    private class AdminTripAdapter extends RecyclerView.Adapter<AdminTripAdapter.AdminHolder> {

        @NonNull
        @Override
        public AdminHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_trip, parent, false);
            return new AdminHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AdminHolder holder, int position) {
            Trip t = tripsList.get(position);
            holder.tvDest.setText(t.getDestination());
            holder.tvPrice.setText("$" + t.getPrice());

            holder.btnEdit.setOnClickListener(v -> showTripDialog(t));
            holder.btnDelete.setOnClickListener(v -> {
                dbHelper.deleteTrip(t.getId());
                Toast.makeText(getContext(), "Destination dropped", Toast.LENGTH_SHORT).show();
                refreshData();
            });
        }

        @Override
        public int getItemCount() { return tripsList.size(); }

        class AdminHolder extends RecyclerView.ViewHolder {
            TextView tvDest, tvPrice;
            Button btnEdit, btnDelete;
            AdminHolder(View iv) {
                super(iv);
                tvDest = iv.findViewById(R.id.admin_item_destination);
                tvPrice = iv.findViewById(R.id.admin_item_price);
                btnEdit = iv.findViewById(R.id.btn_item_edit);
                btnDelete = iv.findViewById(R.id.btn_item_delete);
            }
        }
    }
}