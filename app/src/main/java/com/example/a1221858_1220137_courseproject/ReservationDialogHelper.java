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

// ReservationDialogHelper shows a popup dialog so the user can book a trip
// Uses AlertDialog
public class ReservationDialogHelper {

    private Context context;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    public ReservationDialogHelper(Context context) {
        this.context        = context;
        this.databaseHelper = new DatabaseHelper(context, "TripApp.db", null, 1);
        this.sessionManager = SessionManager.getInstance(context);
    }

    // Show the reservation dialog for a specific trip
    // tripId and tripName come from the trip the user clicked on
    public void showReservationDialog(int tripId, String tripName) {

        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_reservation_form, null);

        // Connect views inside the dialog
        TextView textViewTripName  = dialogView.findViewById(R.id.textView_dialog_tripName);
        EditText editTextQuantity  = dialogView.findViewById(R.id.editText_quantity);
        Spinner spinnerType        = dialogView.findViewById(R.id.spinner_reservationType);
        Button buttonConfirm       = dialogView.findViewById(R.id.button_confirmReservation);
        Button buttonCancel        = dialogView.findViewById(R.id.button_cancelReservation);

        // Show the trip name in the dialog
        textViewTripName.setText("Trip: " + tripName);

        // Fill the reservation type spinner with options
        String[] typeOptions = {"Select Type", "Solo", "Group", "Family", "Business"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                typeOptions
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        // Build the AlertDialog using the inflated view
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(false); // user must press confirm or cancel

        AlertDialog dialog = builder.create();
        dialog.show();

        // Confirm button click — validate and save reservation
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate quantity field is not empty
                if (InputValidator.isEmpty(editTextQuantity)) {
                    editTextQuantity.setError("Please enter number of seats");
                    return;
                }

                // Validate quantity is a valid number greater than 0
                if (!InputValidator.isValidQuantity(editTextQuantity)) {
                    editTextQuantity.setError("Quantity must be greater than 0");
                    return;
                }

                // Validate reservation type is selected
                if (spinnerType.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, "Please select a reservation type", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get values from form
                int quantity   = Integer.parseInt(editTextQuantity.getText().toString().trim());
                String type    = spinnerType.getSelectedItem().toString();
                String status  = "Confirmed";

                // Get today's date as the reservation date
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // Get the logged-in user's ID from session
                int userId = sessionManager.getUserId();

                // Build the Reservation object and save it to the database
                Reservation reservation = new Reservation(
                        0,       // ID will be auto assigned
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

        // Cancel button — just close the dialog
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}