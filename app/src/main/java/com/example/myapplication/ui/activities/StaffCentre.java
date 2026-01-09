package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.model.Reservation;
import com.example.myapplication.data.repository.ReservationRepository;
import com.example.myapplication.data.repository.ReservationRepositoryImpl;
import com.example.myapplication.session.SessionManager;
import com.example.myapplication.ui.adapters.StaffResAdapter;
import com.example.myapplication.ui.viewmodel.StaffAdminViewModel;

public class StaffCentre extends AppCompatActivity {
    private StaffResAdapter staffResAdapter;
    private StaffAdminViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            Intent loginIntent = new Intent(this, stafflogin.class);
            startActivity(loginIntent);
            finish();
            return;
        }
        if (!"staff".equalsIgnoreCase(session.getUsertype())) {
            Intent guestIntent = new Intent(this, GuestProfileActivity.class);
            startActivity(guestIntent);
            finish();
            return;
        }
        AppDatabase database = AppDatabase.getDatabase(this);
        ReservationRepository reservationRepository = new ReservationRepositoryImpl(
                database.reservationsDao()
        );
        viewModel = new ViewModelProvider(
                this,
                new StaffAdminViewModel.Factory(reservationRepository)
        ).get(StaffAdminViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_centre);
        TextView staffReservations = findViewById(R.id.textView99);
        staffReservations.setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffReservations.class);
            startActivity(intent);
        });
        ImageView staffMenu = findViewById(R.id.imageView18);
        staffMenu.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, StaffMenu.class);
            startActivity(intent2);
        });
        Button manageUsers = findViewById(R.id.button_manage_users);
        manageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffUserManagementActivity.class);
            startActivity(intent);
        });

        ImageButton notifButton = findViewById(R.id.imageButton16);
        notifButton.setOnClickListener(v -> newReservations());

        staffResAdapter = new StaffResAdapter(this::deleteReservation);
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MINUTE) >= 30) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        Calendar currentCalendar = Calendar.getInstance();

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String upcomingRes = timeFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(currentCalendar.getTime());

        TextView currentTimeView = findViewById(R.id.textView59);
        currentTimeView.setText(currentTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String todaysDate = dateFormat.format(Calendar.getInstance().getTime());

        RecyclerView recyclerView = findViewById(R.id.centreView);
        recyclerView.setAdapter(staffResAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingRes = "12:55";
        List<Reservation> reservations = viewModel.getReservationsForDate(todaysDate);
        if (reservations != null) {
            staffResAdapter.submitList(reservations);
        }
    }

    private void newReservations() {
        View dialogView = getLayoutInflater().inflate(R.layout.popup_newres, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        ImageButton closeButton = dialogView.findViewById(R.id.imageButton20);
        ImageButton backButton = dialogView.findViewById(R.id.imageButton21);
        Button placeholderButton = dialogView.findViewById(R.id.button28);

        androidx.constraintlayout.widget.Group viewAllGroup = dialogView.findViewById(R.id.ViewAll);
        androidx.constraintlayout.widget.Group viewOneGroup = dialogView.findViewById(R.id.ViewOne);
        viewAllGroup.setVisibility(View.VISIBLE);
        viewOneGroup.setVisibility(View.GONE);
        dialog.show();
        closeButton.setOnClickListener(v -> dialog.dismiss());
        placeholderButton.setOnClickListener(v -> {
            viewAllGroup.setVisibility(View.GONE);
            viewOneGroup.setVisibility(View.VISIBLE);
        });
        backButton.setOnClickListener(v -> {
            viewAllGroup.setVisibility(View.VISIBLE);
            viewOneGroup.setVisibility(View.GONE);
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
            viewModel.deleteReservation(reservation);
            dialog.dismiss();
        });
        noButton.setOnClickListener(v -> dialog.dismiss());
    }
}
