package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.CRUDintoMethods;
import com.example.myapplication.data.model.Reservation;
import com.example.myapplication.ui.adapters.StaffResAdapter;
import java.util.ArrayList;

public class SearchByName extends AppCompatActivity {
    private StaffResAdapter staffResAdapter;
    private AppDatabase database;
    private CRUDintoMethods repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getDatabase(this);
        repository = new CRUDintoMethods(
                database.menuItemDao(),
                database.tablesDao(),
                database.tablesAvailabilityDao(),
                database.reservationsDao()
        );

        EdgeToEdge.enable(this);
        setContentView(R.layout.search_res);

        ImageButton back = findViewById(R.id.imageButton18);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffReservations.class);
            startActivity(intent);
        });
        staffResAdapter = new StaffResAdapter(this::deleteReservation);
        Button search = findViewById(R.id.button21);
        TextInputEditText searchInput = findViewById(R.id.nameField);
        RecyclerView recyclerView = findViewById(R.id.sbnView);
        recyclerView.setAdapter(staffResAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search.setOnClickListener(v -> {
            String name = searchInput.getText() != null ? searchInput.getText().toString() : "";
            if (!name.isEmpty()) {
                List<Reservation> reservations = repository.getReservationsByName(name);
                staffResAdapter.submitList(reservations);
            }
        });
    }

    private void deleteReservation(Reservation reservation) {
        View dialogView = getLayoutInflater().inflate(R.layout.popup_deleteres, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        ImageButton closeButton = dialogView.findViewById(R.id.imageButton22);
        Button deleteButton = dialogView.findViewById(R.id.button17);
        Button noButton = dialogView.findViewById(R.id.button25);
        TextView nameView = dialogView.findViewById(R.id.textView85);
        TextView dateView = dialogView.findViewById(R.id.textView45);
        TextView timeView = dialogView.findViewById(R.id.textView90);
        nameView.setText(reservation.name);
        dateView.setText(reservation.date);
        timeView.setText(reservation.time);

        dialog.show();
        closeButton.setOnClickListener(v -> dialog.dismiss());
        deleteButton.setOnClickListener(v -> {
            List<Reservation> currentList = new ArrayList<>(staffResAdapter.getCurrentList());
            currentList.remove(reservation);
            staffResAdapter.submitList(currentList);
            repository.deleteReservation(reservation);
            dialog.dismiss();
        });
        noButton.setOnClickListener(v -> dialog.dismiss());
    }
}
