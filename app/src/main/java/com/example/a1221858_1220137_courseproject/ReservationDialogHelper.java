package com.example.a1221858_1220137_courseproject;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservationDialogHelper {

    private Context context;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    public ReservationDialogHelper(Context context) {
        this.context        = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.sessionManager = SessionManager.getInstance(context);
    }

    public void showReservationDialog(int tripId, String tripName) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_reservation_form, null);

        TextView textViewTripName  = dialogView.findViewById(R.id.textView_dialog_tripName);
        EditText editTextQuantity  = dialogView.findViewById(R.id.editText_quantity);
        Spinner spinnerType        = dialogView.findViewById(R.id.spinner_reservationType);
        Button buttonConfirm       = dialogView.findViewById(R.id.button_confirmReservation);
        Button buttonCancel        = dialogView.findViewById(R.id.button_cancelReservation);

        textViewTripName.setText("Trip: " + tripName);

        String[] typeOptions = {"Select Type", "Solo", "Group", "Family", "Business"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                typeOptions
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputValidator.isEmpty(editTextQuantity)) {
                    editTextQuantity.setError("Please enter number of seats");
                    return;
                }

                if (!InputValidator.isValidQuantity(editTextQuantity)) {
                    editTextQuantity.setError("Quantity must be greater than 0");
                    return;
                }

                if (spinnerType.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, "Please select a reservation type", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity   = Integer.parseInt(editTextQuantity.getText().toString().trim());
                String type    = spinnerType.getSelectedItem().toString();
                String status  = "Confirmed";
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                int userId = sessionManager.getUserId();

                Reservation reservation = new Reservation(
                        0,
                        userId,
                        tripId,
                        quantity,
                        type,
                        date,
                        status
                );

                databaseHelper.insertReservation(reservation);

                Toast.makeText(context, "Reservation confirmed!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
