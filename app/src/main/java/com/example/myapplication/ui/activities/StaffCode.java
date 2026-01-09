package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.google.android.material.textfield.TextInputEditText;
import com.example.myapplication.R;

public class StaffCode extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_login3);
        Group resetPswd = findViewById(R.id.EnterPswd);
        ImageButton back = findViewById(R.id.imageButton9);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, stafflogin.class);
            startActivity(intent);
        });
        TextInputEditText codeInput = findViewById(R.id.textInputEditText4);
        TextInputEditText pswd1 = findViewById(R.id.editText4);
        TextInputEditText pswd2 = findViewById(R.id.editText5);
        TextView warning = findViewById(R.id.textView75);
        codeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("XXXXXX".contentEquals(s)) {
                    resetPswd.setVisibility(View.VISIBLE);
                    codeInput.setVisibility(View.GONE);
                    Button resetButton = findViewById(R.id.button22);
                    resetButton.setOnClickListener(view -> {
                        String field1 = pswd1.getText() != null ? pswd1.getText().toString() : "";
                        String field2 = pswd2.getText() != null ? pswd2.getText().toString() : "";
                        if (!field1.isEmpty()) {
                            if (!field2.isEmpty()) {
                                if (field1.equals(field2)) {
                                    resetPswd.setVisibility(View.GONE);
                                    TextView text = findViewById(R.id.textView74);
                                    text.setText("Password reset successfully. Feel free to close this page and login");
                                } else {
                                    warning.setText("Passwords must match");
                                }
                            } else {
                                pswd2.setHintTextColor(Color.parseColor("#FB0303"));
                                pswd2.setHint("*Required");
                                warning.setText("Both fields must be complete");
                            }
                        } else {
                            pswd1.setHint("*Required");
                            warning.setText("Both fields must be complete");
                            if (field2.isEmpty()) {
                                pswd2.setHint("*Required");
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
