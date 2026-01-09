package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.session.SessionManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button res = findViewById(R.id.button);
        res.setOnClickListener(v -> {
            Intent intent = new Intent(this, Reservations.class);
            startActivity(intent);
        });

        Button menu = findViewById(R.id.button4);
        menu.setOnClickListener(v -> {
            Intent gotomenu = new Intent(this, Menu.class);
            startActivity(gotomenu);
        });

        Button login = findViewById(R.id.button5);
        login.setOnClickListener(v -> {
            Intent intent = new Intent(this, stafflogin.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            Intent intent;
            if ("staff".equalsIgnoreCase(sessionManager.getUsertype())) {
                intent = new Intent(this, StaffCentre.class);
            } else {
                intent = new Intent(this, GuestProfileActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }
}
