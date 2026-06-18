package com.example.a1221858_1220137_courseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// ReservationAdapter connects the list of reservations to the RecyclerView
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private Context context;
    private List<Reservation> reservationList;
    private List<String> tripNameList; // trip names matching each reservation

    // Constructor receives the context, reservations, and trip names
    public ReservationAdapter(Context context, List<Reservation> reservationList, List<String> tripNameList) {
        this.context       = context;
        this.reservationList = reservationList;
        this.tripNameList  = tripNameList;
    }

    // Called when RecyclerView needs a new card view — inflate item_reservation layout
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    // Called to fill each card with data from the reservation list
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        // Set trip name from the matching trip name list
        holder.textViewTripName.setText(tripNameList.get(position));

        // Set reservation details
        holder.textViewDate.setText("Date: "     + reservation.getDate());
        holder.textViewType.setText("Type: "     + reservation.getType());
        holder.textViewQuantity.setText("Quantity: " + reservation.getQuantity());
        holder.textViewStatus.setText(reservation.getStatus());
    }

    // Returns how many items are in the list
    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    // ViewHolder holds references to all the views in one card
    // This avoids calling findViewById repeatedly — better performance
    public static class ReservationViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTripName;
        TextView textViewDate;
        TextView textViewType;
        TextView textViewQuantity;
        TextView textViewStatus;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTripName = itemView.findViewById(R.id.textView_tripName);
            textViewDate     = itemView.findViewById(R.id.textView_reservationDate);
            textViewType     = itemView.findViewById(R.id.textView_reservationType);
            textViewQuantity = itemView.findViewById(R.id.textView_quantity);
            textViewStatus   = itemView.findViewById(R.id.textView_status);
        }
    }
}