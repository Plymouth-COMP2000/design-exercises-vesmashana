package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.core.MyRestaurantApplication;
import com.example.myapplication.data.model.User;
import com.example.myapplication.data.repository.UserRepository;
import com.example.myapplication.session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class GuestProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.guest_profile);

        SessionManager sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, stafflogin.class));
            finish();
            return;
        }
        if (!"student".equalsIgnoreCase(sessionManager.getUsertype())) {
            startActivity(new Intent(this, StaffCentre.class));
            finish();
            return;
        }

        TextView usernameView = findViewById(R.id.guest_profile_username);
        TextView usertypeView = findViewById(R.id.guest_profile_usertype);
        TextInputEditText firstnameField = findViewById(R.id.guest_firstname);
        TextInputEditText lastnameField = findViewById(R.id.guest_lastname);
        TextInputEditText emailField = findViewById(R.id.guest_email);
        TextInputEditText contactField = findViewById(R.id.guest_contact);
        TextInputEditText passwordField = findViewById(R.id.guest_password);
        TextInputLayout firstnameLayout = findViewById(R.id.guest_firstname_layout);
        TextInputLayout lastnameLayout = findViewById(R.id.guest_lastname_layout);
        TextInputLayout emailLayout = findViewById(R.id.guest_email_layout);
        TextInputLayout contactLayout = findViewById(R.id.guest_contact_layout);
        TextInputLayout passwordLayout = findViewById(R.id.guest_password_layout);

        usernameView.setText("Username: " + sessionManager.getUsername());
        usertypeView.setText("Role: " + sessionManager.getUsertype());
        firstnameField.setText(sessionManager.getFirstname());
        lastnameField.setText(sessionManager.getLastname());

        MyRestaurantApplication application = (MyRestaurantApplication) getApplication();
        UserRepository userRepository = application.getUserRepository();

        userRepository.fetchUser(sessionManager.getUsername(), new UserRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {
                firstnameField.setText(user.firstname);
                lastnameField.setText(user.lastname);
                emailField.setText(user.email);
                contactField.setText(user.contact);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(GuestProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        Button updateButton = findViewById(R.id.guest_update_button);
        updateButton.setOnClickListener(v -> {
            firstnameLayout.setError(null);
            lastnameLayout.setError(null);
            emailLayout.setError(null);
            contactLayout.setError(null);
            passwordLayout.setError(null);

            String username = sessionManager.getUsername();
            String firstname = textValue(firstnameField);
            String lastname = textValue(lastnameField);
            String email = textValue(emailField);
            String contact = textValue(contactField);
            String password = textValue(passwordField);

            if (firstname.isEmpty()) {
                firstnameLayout.setError("First name is required.");
                return;
            }
            if (lastname.isEmpty()) {
                lastnameLayout.setError("Last name is required.");
                return;
            }
            if (password.isEmpty()) {
                passwordLayout.setError("Password is required.");
                return;
            }

            User updated = new User(username, password, firstname, lastname, email, contact, "student");
            updateButton.setEnabled(false);
            userRepository.updateUser(username, updated, new UserRepository.UserCallback() {
                @Override
                public void onSuccess(User user) {
                    updateButton.setEnabled(true);
                    sessionManager.saveUser(user);
                    Toast.makeText(GuestProfileActivity.this, "Profile updated.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String message) {
                    updateButton.setEnabled(true);
                    Toast.makeText(GuestProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        Button logoutButton = findViewById(R.id.guest_logout_button);
        logoutButton.setOnClickListener(v -> {
            sessionManager.clear();
            startActivity(new Intent(this, stafflogin.class));
            finish();
        });
    }

    private String textValue(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
