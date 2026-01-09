package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.CRUDintoMethods;
import com.example.myapplication.data.model.Reservation;

public class ConfirmBooking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppDatabase database = AppDatabase.getDatabase(this);
        CRUDintoMethods repository = new CRUDintoMethods(
                database.menuItemDao(),
                database.tablesDao(),
                database.tablesAvailabilityDao(),
                database.reservationsDao()
        );
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.confirmbooking);

        String date = getIntent().getStringExtra("date");
        String timeslot = getIntent().getStringExtra("timeslot");
        String name = getIntent().getStringExtra("name");
        TextView displayDate = findViewById(R.id.textView9);
        displayDate.setText(date);
        TextView displayTimeslot = findViewById(R.id.textView8);
        displayTimeslot.setText(timeslot);

        ImageButton back = findViewById(R.id.imageButton);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Reservations.class);
            startActivity(intent);
        });
        Button placeBooking = findViewById(R.id.button6);
        placeBooking.setOnClickListener(v -> {
            CheckBox checkboxWindow = findViewById(R.id.checkBox);
            CheckBox checkboxBar = findViewById(R.id.checkBox2);
            TextInputEditText peopleEntry = findViewById(R.id.textInputEditText2);
            boolean wantsWindowSeat = checkboxWindow.isChecked();
            boolean wantsBarSeat = checkboxBar.isChecked();

            Integer peopleCount = null;
            if (peopleEntry.getText() != null) {
                try {
                    peopleCount = Integer.parseInt(peopleEntry.getText().toString());
                } catch (NumberFormatException ignored) {
                }
            }

            if (peopleCount != null) {
                if (peopleCount < 15) {
                    Reservation reservation = new Reservation(
                            name == null ? "" : name,
                            "",
                            date == null ? "" : date,
                            timeslot == null ? "" : timeslot,
                            peopleCount,
                            wantsWindowSeat,
                            wantsBarSeat
                    );
                    try {
                        long reservationID = repository.insertReservation(reservation);
                        Intent intent = new Intent(ConfirmBooking.this, Success.class);
                        intent.putExtra("reservationID", reservationID);
                        startActivity(intent);
                        Log.e("ConfirmBooking", "Reservation inserted successfully with ID: " + reservationID);
                    } catch (Exception e) {
                        Log.e("ConfirmBooking", "Failed to insert reservation", e);
                    }
                } else {
                    TextView warning = findViewById(R.id.textView71);
                    warning.setText("*We don't currently accept reservations of over 14 people");
                }
            } else {
                TextView warning = findViewById(R.id.textView71);
                warning.setText("*This is a required field");
            }
        });
    }
}
