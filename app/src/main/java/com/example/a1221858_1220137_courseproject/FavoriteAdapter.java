package com.example.a1221858_1220137_courseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<Trip> favoriteTrips;

    public interface FavoriteActionListener {
        void onRemoveFavorite(Trip trip, int position);
        void onBookTrip(Trip trip);
    }

    private FavoriteActionListener listener;

    public FavoriteAdapter(Context context, List<Trip> favoriteTrips, FavoriteActionListener listener) {
        this.context       = context;
        this.favoriteTrips = favoriteTrips;
        this.listener      = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Trip trip = favoriteTrips.get(position);

        holder.textViewDestination.setText(trip.getDestination());
        holder.textViewDuration.setText("Duration: " + trip.getDuration());
        holder.textViewPrice.setText("Price: $" + trip.getPrice());

        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                listener.onRemoveFavorite(trip, currentPosition);
            }
        });

        holder.buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBookTrip(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteTrips.size();
    }

    public void removeItem(int position) {
        favoriteTrips.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favoriteTrips.size());
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDestination;
        TextView textViewDuration;
        TextView textViewPrice;
        Button buttonBook;
        Button buttonRemove;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDestination = itemView.findViewById(R.id.textView_fav_destination);
            textViewDuration    = itemView.findViewById(R.id.textView_fav_duration);
            textViewPrice       = itemView.findViewById(R.id.textView_fav_price);
            buttonBook          = itemView.findViewById(R.id.button_fav_book);
            buttonRemove        = itemView.findViewById(R.id.button_fav_remove);
        }
    }
}
