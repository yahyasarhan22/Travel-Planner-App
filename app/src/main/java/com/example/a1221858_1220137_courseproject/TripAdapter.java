package com.example.a1221858_1220137_courseproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a1221858_1220137_courseproject.R;
import com.example.a1221858_1220137_courseproject.Trip;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private final List<Trip> tripsList;
    private final OnTripClickListener clickListener;

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }

    public TripAdapter(List<Trip> tripsList, OnTripClickListener clickListener) {
        this.tripsList = tripsList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripsList.get(position);
        holder.tvDestination.setText(trip.getDestination());
        holder.tvDescription.setText(trip.getDescription());
        holder.tvDuration.setText(trip.getDuration());
        holder.tvPrice.setText("$" + trip.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onTripClick(trip);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestination, tvDescription, tvDuration, tvPrice;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.item_tv_destination);
            tvDescription = itemView.findViewById(R.id.item_tv_description);
            tvDuration = itemView.findViewById(R.id.item_tv_duration);
            tvPrice = itemView.findViewById(R.id.item_tv_price);
        }
    }
}