package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class ReservationsLog extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_log);
        ImageButton back = findViewById(R.id.imageButton19);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffReservations.class);
            startActivity(intent);
        });
    }
}
