package com.example.a1221858_1220137_courseproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private final List<Trip> tripsList;
    private final OnTripClickListener clickListener;
    private final DatabaseHelper dbHelper;
    private final int currentUserId;

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }

    public TripAdapter(List<Trip> tripsList, DatabaseHelper dbHelper, int currentUserId, OnTripClickListener clickListener) {
        this.tripsList = tripsList;
        this.dbHelper = dbHelper;
        this.currentUserId = currentUserId;
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
        holder.tvCountry.setText(trip.getCountry());
        holder.tvDuration.setText(trip.getDurationDays() + " Days");
        holder.tvPrice.setText("$" + trip.getPrice());
        holder.tvRating.setText("⭐ " + trip.getRating());

        Glide.with(holder.itemView.getContext())
                .load(trip.getImageUrl())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivTripGraphic);

        if (dbHelper.isFavorite(currentUserId, trip.getId())) {
            holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }

        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelper.isFavorite(currentUserId, trip.getId())) {
                    dbHelper.removeFavorite(currentUserId, trip.getId());
                    holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                } else {
                    dbHelper.addFavorite(currentUserId, trip.getId());
                    holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                }
            }
        });

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
        ImageView ivTripGraphic;
        TextView tvDestination, tvCountry, tvDuration, tvPrice, tvRating;
        ImageButton btnFavorite;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTripGraphic = itemView.findViewById(R.id.imageView_trip);
            tvDestination = itemView.findViewById(R.id.textView_destination);
            tvCountry     = itemView.findViewById(R.id.textView_country);
            tvDuration    = itemView.findViewById(R.id.textView_duration);
            tvPrice       = itemView.findViewById(R.id.textView_price);
            tvRating      = itemView.findViewById(R.id.textView_rating);
            btnFavorite   = itemView.findViewById(R.id.imageButton_favorite);
        }
    }
}