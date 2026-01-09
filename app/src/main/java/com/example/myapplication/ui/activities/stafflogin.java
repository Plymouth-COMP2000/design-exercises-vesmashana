package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.myapplication.R;
import com.example.myapplication.core.MyRestaurantApplication;
import com.example.myapplication.data.model.User;
import com.example.myapplication.data.repository.UserRepository;
import com.example.myapplication.session.SessionManager;

public class stafflogin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_login1);
        ImageButton back = findViewById(R.id.imageButton8);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        TextView forgotpassword = findViewById(R.id.textView35);
        forgotpassword.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, StaffForgotPassword.class);
            startActivity(intent1);
        });
        Button login = findViewById(R.id.button19);
        TextInputEditText usernameField = findViewById(R.id.username);
        TextInputEditText passwordField = findViewById(R.id.password);
        TextInputLayout usernameLayout = findViewById(R.id.username_layout);
        TextInputLayout passwordLayout = findViewById(R.id.password_layout);
        login.setOnClickListener(v -> {
            usernameLayout.setError(null);
            passwordLayout.setError(null);
            String username = textValue(usernameField);
            String password = textValue(passwordField);
            if (username.isEmpty()) {
                usernameLayout.setError("Enter a username");
                return;
            }
            if (password.isEmpty()) {
                passwordLayout.setError("Enter a password");
                return;
            }
            login.setEnabled(false);
            MyRestaurantApplication application = (MyRestaurantApplication) getApplication();
            UserRepository userRepository = application.getUserRepository();
            userRepository.login(username, password, new UserRepository.UserCallback() {
                @Override
                public void onSuccess(User user) {
                    SessionManager session = new SessionManager(stafflogin.this);
                    session.saveUser(user);
                    Intent intent2;
                    if ("staff".equalsIgnoreCase(user.usertype)) {
                        intent2 = new Intent(stafflogin.this, StaffCentre.class);
                    } else {
                        intent2 = new Intent(stafflogin.this, GuestProfileActivity.class);
                    }
                    startActivity(intent2);
                    finish();
                }

                @Override
                public void onError(String message) {
                    login.setEnabled(true);
                    passwordLayout.setError(message);
                    Toast.makeText(stafflogin.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private String textValue(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
