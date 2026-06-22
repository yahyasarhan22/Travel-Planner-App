package com.example.a1221858_1220137_courseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private Context context;
    private List<Reservation> reservationList;
    private List<String> tripNameList;

    public ReservationAdapter(Context context, List<Reservation> reservationList, List<String> tripNameList) {
        this.context       = context;
        this.reservationList = reservationList;
        this.tripNameList  = tripNameList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        holder.textViewTripName.setText(tripNameList.get(position));
        holder.textViewDate.setText("Date: "     + reservation.getDate());
        holder.textViewType.setText("Type: "     + reservation.getType());
        holder.textViewQuantity.setText("Quantity: " + reservation.getQuantity());
        holder.textViewStatus.setText(reservation.getStatus());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

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
