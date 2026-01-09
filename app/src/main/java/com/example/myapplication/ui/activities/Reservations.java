package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.model.Reservation;
import com.example.myapplication.data.repository.ReservationRepository;
import com.example.myapplication.data.repository.ReservationRepositoryImpl;
import com.example.myapplication.ui.viewmodel.ReservationsViewModel;

public class Reservations extends AppCompatActivity {
    private String date = "";
    private String selected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.reservations);

        AppDatabase database = AppDatabase.getDatabase(this);
        ReservationRepository reservationRepository = new ReservationRepositoryImpl(
                database.reservationsDao()
        );
        ReservationsViewModel viewModel = new ViewModelProvider(
                this,
                new ReservationsViewModel.Factory(reservationRepository)
        ).get(ReservationsViewModel.class);

        ImageButton back = findViewById(R.id.imageButton2);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        TextView emailWarning = findViewById(R.id.textView84);
        TextInputEditText emailInput = findViewById(R.id.emailField);
        Button code = findViewById(R.id.button3);
        code.setOnClickListener(v -> {
            String email = emailInput.getText() != null ? emailInput.getText().toString() : "";
            if (email.isEmpty()) {
                emailWarning.setText("Enter an email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailWarning.setText("Enter a valid email");
            } else {
                Reservation userReservation = viewModel.findFirstReservationByEmail(email);
                if (userReservation == null) {
                    emailWarning.setText("Reservation not found");
                } else {
                    Intent intent2 = new Intent(this, Code.class);
                    intent2.putExtra("NAME", userReservation.name);
                    intent2.putExtra("DATE", userReservation.date);
                    intent2.putExtra("TIME", userReservation.time);
                    intent2.putExtra("pref1", userReservation.pref1);
                    intent2.putExtra("pref2", userReservation.pref2);
                    intent2.putExtra("ReservationID", userReservation.id);
                    int reservationID = userReservation.id;
                    String resName = userReservation.name;
                    Log.e("Reservations", "ReservationID: " + reservationID);
                    Log.e("Reservations", "ResName: " + resName);
                    startActivity(intent2);
                }
            }
        });

        Button confirmbooking = findViewById(R.id.button2);
        confirmbooking.setOnClickListener(v -> {
            TextInputEditText nameView = findViewById(R.id.nameEditText);
            String name = nameView.getText() != null ? nameView.getText().toString() : "";
            if (name.isEmpty()) {
                TextView warning1 = findViewById(R.id.textView70);
                warning1.setText("*This is a required field");
            } else if (date.isEmpty()) {
                TextView warning2 = findViewById(R.id.textView65);
                warning2.setText("*This is a required field");
            } else if (selected.isEmpty()) {
                TextView warning3 = findViewById(R.id.textView64);
                warning3.setText("*This is a required field");
            } else {
                Intent intent2 = new Intent(this, ConfirmBooking.class);
                intent2.putExtra("name", name);
                intent2.putExtra("timeslot", selected);
                intent2.putExtra("date", date);
                startActivity(intent2);
            }
        });

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        calendarView.setMaxDate(System.currentTimeMillis() + 31536000000L);
        List<String> timeslots = Arrays.asList("12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        List<TextView> views = Arrays.asList(
                findViewById(R.id.textView11),
                findViewById(R.id.textView12),
                findViewById(R.id.textView13),
                findViewById(R.id.textView16),
                findViewById(R.id.textView66),
                findViewById(R.id.textView67),
                findViewById(R.id.textView68),
                findViewById(R.id.textView69)
        );

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            date = dateFormat.format(calendar.getTime());

            for (int i = 0; i < timeslots.size(); i++) {
                views.get(i).setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
            }
            for (int i = 0; i < timeslots.size(); i++) {
                String timeSlot = timeslots.get(i);
                TextView timeView = views.get(i);
                timeView.setText(timeSlot);
                int index = i;
                timeView.setOnClickListener(v -> {
                    selected = timeslots.get(index);
                    for (TextView textView : views) {
                        textView.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
                    }
                    timeView.setBackgroundColor(android.graphics.Color.parseColor("#5DB2F6"));
                });
            }
        });
    }
}
