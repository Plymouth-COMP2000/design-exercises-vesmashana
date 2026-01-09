package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class Success extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.success);
        AppDatabase database = AppDatabase.getDatabase(this);
        CRUDintoMethods repository = new CRUDintoMethods(
                database.menuItemDao(),
                database.tablesDao(),
                database.tablesAvailabilityDao(),
                database.reservationsDao()
        );

        ImageButton back = findViewById(R.id.imageButton3);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        long reservationID = getIntent().getLongExtra("reservationID", -1);
        TextInputEditText emailField = findViewById(R.id.editText);
        TextView prompt = findViewById(R.id.textView22);
        CheckBox notifications = findViewById(R.id.checkBox3);

        Button go = findViewById(R.id.button7);
        go.setOnClickListener(v -> {
            String email = emailField.getText() != null ? emailField.getText().toString() : "";
            if (!email.isEmpty()) {
                prompt.setText("You may now close this window.");
                emailField.setVisibility(View.GONE);
                notifications.setVisibility(View.GONE);
                go.setVisibility(View.GONE);

                Reservation originalReservation = repository.getReservationById(reservationID);
                if (originalReservation != null) {
                    originalReservation.email = email;
                    repository.updateReservation(originalReservation);
                } else {
                    prompt.setText("error");
                }
            }
        });
    }
}
