package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.example.myapplication.R;

public class Code extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.code);
        String date = getIntent().getStringExtra("DATE");
        String time = getIntent().getStringExtra("TIME");
        String name = getIntent().getStringExtra("NAME");
        boolean pref1 = getIntent().getBooleanExtra("pref1", false);
        boolean pref2 = getIntent().getBooleanExtra("pref2", false);
        int reservationID = getIntent().getIntExtra("ReservationID", -1);
        Log.e("Code", "ReservationID: " + reservationID);
        Log.e("Code", "NAME: " + name);

        ImageButton login = findViewById(R.id.imageButton4);
        login.setOnClickListener(v -> {
            Intent intent = new Intent(this, Reservations.class);
            startActivity(intent);
        });
        TextInputEditText codeInput = findViewById(R.id.textInputEditText);
        codeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("XXXX".contentEquals(s)) {
                    Intent intent2 = new Intent(Code.this, YourReservation.class);
                    intent2.putExtra("NAME", name);
                    intent2.putExtra("DATE", date);
                    intent2.putExtra("TIME", time);
                    intent2.putExtra("pref1", pref1);
                    intent2.putExtra("pref2", pref2);
                    intent2.putExtra("ReservationID", reservationID);
                    startActivity(intent2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
