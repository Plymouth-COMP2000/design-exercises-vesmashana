package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import java.util.Arrays;
import java.util.List;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.CRUDintoMethods;
import com.example.myapplication.data.model.Reservation;

public class YourReservation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppDatabase database = AppDatabase.getDatabase(this);
        CRUDintoMethods repository = new CRUDintoMethods(
                database.menuItemDao(),
                database.tablesDao(),
                database.tablesAvailabilityDao(),
                database.reservationsDao()
        );

        String dateExtra = getIntent().getStringExtra("DATE");
        String timeExtra = getIntent().getStringExtra("TIME");
        String nameExtra = getIntent().getStringExtra("NAME");
        boolean pref1 = getIntent().getBooleanExtra("pref1", false);
        boolean pref2 = getIntent().getBooleanExtra("pref2", false);
        int reservationID = getIntent().getIntExtra("ReservationID", -1);
        Log.e("YourReservation", "ReservationID: " + reservationID);

        Intent intent2 = new Intent(this, Success.class);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.yourreservation);
        TextView nameDisplay = findViewById(R.id.textView26);
        nameDisplay.setText(nameExtra);
        TextView dateDisplay = findViewById(R.id.textView27);
        dateDisplay.setText(dateExtra);
        TextView timeDisplay = findViewById(R.id.textView28);
        timeDisplay.setText(timeExtra);
        ImageButton back = findViewById(R.id.imageButton5);
        CheckBox pref1Box = findViewById(R.id.checkBox4);
        CheckBox pref2Box = findViewById(R.id.checkBox5);

        pref1Box.setChecked(pref1);
        pref2Box.setChecked(pref2);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Reservations.class);
            startActivity(intent);
        });

        Group dtGroup = findViewById(R.id.date_time_group);
        Group prefGroup = findViewById(R.id.preferences_group);
        Group cancelGroup = findViewById(R.id.cancel_group);
        Button confirmButton = findViewById(R.id.button11);
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        calendarView.setMaxDate(System.currentTimeMillis() + 31536000000L);
        final String[] date = {""};
        final String[] selected = {""};
        List<String> timeslots = Arrays.asList("12:30", "13:00", "14:00", "15:30", "16:30", "17:00", "18:00");
        List<TextView> views = Arrays.asList(
                findViewById(R.id.textView31),
                findViewById(R.id.textView32),
                findViewById(R.id.textView33),
                findViewById(R.id.textView34),
                findViewById(R.id.textView40),
                findViewById(R.id.textView41),
                findViewById(R.id.textView42),
                findViewById(R.id.textView43)
        );

        Button datetime = findViewById(R.id.button8);
        datetime.setOnClickListener(v -> {
            dtGroup.setVisibility(View.VISIBLE);
            prefGroup.setVisibility(View.GONE);
            cancelGroup.setVisibility(View.GONE);
            confirmButton.setVisibility(View.VISIBLE);
            TextView warning1 = findViewById(R.id.textView76);
            TextView warning2 = findViewById(R.id.textView77);
            confirmButton.setOnClickListener(confirmView -> {
                if (date[0].isEmpty()) {
                    warning1.setText("*This is a required field");
                } else if (selected[0].isEmpty()) {
                    warning2.setText("*This is a required field");
                } else {
                    Reservation originalReservation = repository.getReservationById(reservationID);
                    if (originalReservation != null) {
                        originalReservation.date = date[0];
                        originalReservation.time = selected[0];
                        repository.updateReservation(originalReservation);
                        startActivity(intent2);
                    }
                }
            });
        });

        Button preferences = findViewById(R.id.button9);
        preferences.setOnClickListener(v -> {
            prefGroup.setVisibility(View.VISIBLE);
            dtGroup.setVisibility(View.GONE);
            cancelGroup.setVisibility(View.GONE);
            confirmButton.setVisibility(View.VISIBLE);
            confirmButton.setOnClickListener(confirmView -> {
                Reservation originalReservation = repository.getReservationById(reservationID);
                if (originalReservation != null) {
                    originalReservation.pref1 = pref1Box.isChecked();
                    originalReservation.pref2 = pref2Box.isChecked();
                    repository.updateReservation(originalReservation);
                    startActivity(intent2);
                }
            });
        });

        Button cancel = findViewById(R.id.button10);
        cancel.setOnClickListener(v -> {
            cancelGroup.setVisibility(View.VISIBLE);
            dtGroup.setVisibility(View.GONE);
            prefGroup.setVisibility(View.GONE);
            confirmButton.setVisibility(View.VISIBLE);
            confirmButton.setOnClickListener(confirmView -> {
                Reservation originalReservation = repository.getReservationById(reservationID);
                if (originalReservation != null) {
                    repository.deleteReservation(originalReservation);
                }
            });
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            date[0] = dayOfMonth + "-" + (month + 1) + "-" + year;
            for (TextView viewItem : views) {
                viewItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            for (int i = 0; i < timeslots.size(); i++) {
                String timeSlot = timeslots.get(i);
                TextView slotView = views.get(i);
                slotView.setText(timeSlot);
                int index = i;
                slotView.setOnClickListener(v -> {
                    selected[0] = timeslots.get(index);
                    for (TextView viewItem : views) {
                        viewItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    slotView.setBackgroundColor(Color.parseColor("#5DB2F6"));
                });
            }
        });
    }
}
