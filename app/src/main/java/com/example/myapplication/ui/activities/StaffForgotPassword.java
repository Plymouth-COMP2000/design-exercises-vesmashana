package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class StaffForgotPassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_login2);
        ImageButton back = findViewById(R.id.imageButton9);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, stafflogin.class);
            startActivity(intent);
        });
        Button sendcode = findViewById(R.id.button20);
        sendcode.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, StaffCode.class);
            startActivity(intent1);
        });
    }
}
