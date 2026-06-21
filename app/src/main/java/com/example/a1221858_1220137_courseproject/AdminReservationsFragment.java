package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AdminReservationsFragment extends Fragment {

    private EditText etSearchReservations;
    private TextView tvTotalReservationsCount;
    private RecyclerView rvAdminReservations;

    private DatabaseHelper dbHelper;
    private List<DatabaseHelper.ReservationDetail> masterList = new ArrayList<>();
    private List<DatabaseHelper.ReservationDetail> filteredList = new ArrayList<>();
    private ReservationsAdapter adapter;

    public AdminReservationsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_reservations, container, false);

        etSearchReservations = view.findViewById(R.id.et_search_reservations);
        tvTotalReservationsCount = view.findViewById(R.id.tv_total_reservations_count);
        rvAdminReservations = view.findViewById(R.id.rv_admin_reservations);

        rvAdminReservations.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new DatabaseHelper(getContext());

        refreshList();

        etSearchReservations.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterReservations(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void refreshList() {
        masterList.clear();
        masterList.addAll(dbHelper.getAllReservationsWithDetails());

        filteredList.clear();
        filteredList.addAll(masterList);

        tvTotalReservationsCount.setText(String.valueOf(masterList.size()));

        if (adapter == null) {
            adapter = new ReservationsAdapter();
            rvAdminReservations.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void filterReservations(String query) {
        filteredList.clear();
        String cleanQuery = query.toLowerCase().trim();

        if (cleanQuery.isEmpty()) {
            filteredList.addAll(masterList);
        } else {
            for (DatabaseHelper.ReservationDetail item : masterList) {
                if (item.userName.toLowerCase().contains(cleanQuery) ||
                        item.tripDestination.toLowerCase().contains(cleanQuery)) {
                    filteredList.add(item);
                }
            }
        }

        tvTotalReservationsCount.setText(String.valueOf(filteredList.size()));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationHolder> {

        @NonNull
        @Override
        public ReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_reservation, parent, false);
            return new ReservationHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ReservationHolder holder, int position) {
            DatabaseHelper.ReservationDetail res = filteredList.get(position);

            holder.tvDestination.setText(res.tripDestination + ", " + res.tripCountry);
            holder.tvDate.setText(res.date);
            holder.tvUserInfo.setText("Reserved by: " + res.userName + " (" + res.userEmail + ")");
            holder.tvMetaDetails.setText("Tickets: " + res.quantity + " | Class Type: " + res.type + " | Status: " + res.status);
        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }

        class ReservationHolder extends RecyclerView.ViewHolder {
            TextView tvDestination, tvDate, tvUserInfo, tvMetaDetails;

            ReservationHolder(View itemView) {
                super(itemView);
                tvDestination = itemView.findViewById(R.id.tv_item_dest);
                tvDate = itemView.findViewById(R.id.tv_item_date);
                tvUserInfo = itemView.findViewById(R.id.tv_item_user);
                tvMetaDetails = itemView.findViewById(R.id.tv_item_details);
            }
        }
    }
}