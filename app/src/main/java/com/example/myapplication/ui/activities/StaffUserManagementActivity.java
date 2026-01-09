package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.core.MyRestaurantApplication;
import com.example.myapplication.data.model.User;
import com.example.myapplication.data.repository.UserRepository;
import com.example.myapplication.session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class StaffUserManagementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_user_management);

        SessionManager sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, stafflogin.class));
            finish();
            return;
        }
        if (!"staff".equalsIgnoreCase(sessionManager.getUsertype())) {
            startActivity(new Intent(this, GuestProfileActivity.class));
            finish();
            return;
        }

        MyRestaurantApplication application = (MyRestaurantApplication) getApplication();
        UserRepository userRepository = application.getUserRepository();

        Button viewUsers = findViewById(R.id.staff_view_users_button);
        viewUsers.setOnClickListener(v -> userRepository.fetchAllUsers(new UserRepository.UsersCallback() {
            @Override
            public void onSuccess(List<User> users) {
                StringBuilder builder = new StringBuilder();
                for (User user : users) {
                    builder.append(user.username)
                            .append(" (")
                            .append(user.usertype)
                            .append(")\n");
                }
                new AlertDialog.Builder(StaffUserManagementActivity.this)
                        .setTitle("All Users")
                        .setMessage(builder.length() > 0 ? builder.toString() : "No users found.")
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(StaffUserManagementActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }));

        Button createUser = findViewById(R.id.staff_create_user_button);
        createUser.setOnClickListener(v -> showUserDialog("Create User", null, userRepository, true));

        Button updateUser = findViewById(R.id.staff_update_user_button);
        updateUser.setOnClickListener(v -> showUserDialog("Update User", null, userRepository, false));

        Button deleteUser = findViewById(R.id.staff_delete_user_button);
        deleteUser.setOnClickListener(v -> showDeleteDialog(userRepository));

        Button logout = findViewById(R.id.staff_logout_button);
        logout.setOnClickListener(v -> {
            sessionManager.clear();
            startActivity(new Intent(this, stafflogin.class));
            finish();
        });
    }

    private void showUserDialog(String title, User existing, UserRepository userRepository, boolean isCreate) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_user_form, null);

        TextInputLayout usernameLayout = dialogView.findViewById(R.id.dialog_username_layout);
        TextInputLayout passwordLayout = dialogView.findViewById(R.id.dialog_password_layout);
        TextInputLayout firstnameLayout = dialogView.findViewById(R.id.dialog_firstname_layout);
        TextInputLayout lastnameLayout = dialogView.findViewById(R.id.dialog_lastname_layout);
        TextInputLayout emailLayout = dialogView.findViewById(R.id.dialog_email_layout);
        TextInputLayout contactLayout = dialogView.findViewById(R.id.dialog_contact_layout);
        TextInputLayout usertypeLayout = dialogView.findViewById(R.id.dialog_usertype_layout);

        TextInputEditText usernameField = dialogView.findViewById(R.id.dialog_username);
        TextInputEditText passwordField = dialogView.findViewById(R.id.dialog_password);
        TextInputEditText firstnameField = dialogView.findViewById(R.id.dialog_firstname);
        TextInputEditText lastnameField = dialogView.findViewById(R.id.dialog_lastname);
        TextInputEditText emailField = dialogView.findViewById(R.id.dialog_email);
        TextInputEditText contactField = dialogView.findViewById(R.id.dialog_contact);
        TextInputEditText usertypeField = dialogView.findViewById(R.id.dialog_usertype);

        if (existing != null) {
            usernameField.setText(existing.username);
            passwordField.setText(existing.password);
            firstnameField.setText(existing.firstname);
            lastnameField.setText(existing.lastname);
            emailField.setText(existing.email);
            contactField.setText(existing.contact);
            usertypeField.setText(existing.usertype);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton(isCreate ? "Create" : "Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            usernameLayout.setError(null);
            passwordLayout.setError(null);
            firstnameLayout.setError(null);
            lastnameLayout.setError(null);
            usertypeLayout.setError(null);

            String username = textValue(usernameField);
            String password = textValue(passwordField);
            String firstname = textValue(firstnameField);
            String lastname = textValue(lastnameField);
            String email = textValue(emailField);
            String contact = textValue(contactField);
            String usertype = textValue(usertypeField).toLowerCase();

            if (username.isEmpty()) {
                usernameLayout.setError("Username is required.");
                return;
            }
            if (password.isEmpty()) {
                passwordLayout.setError("Password is required.");
                return;
            }
            if (firstname.isEmpty()) {
                firstnameLayout.setError("First name is required.");
                return;
            }
            if (lastname.isEmpty()) {
                lastnameLayout.setError("Last name is required.");
                return;
            }
            if (!"student".equals(usertype) && !"staff".equals(usertype)) {
                usertypeLayout.setError("User type must be student or staff.");
                return;
            }

            User user = new User(username, password, firstname, lastname, email, contact, usertype);
            if (isCreate) {
                userRepository.createUser(user, new UserRepository.UserCallback() {
                    @Override
                    public void onSuccess(User created) {
                        Toast.makeText(StaffUserManagementActivity.this, "User created.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(StaffUserManagementActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                userRepository.updateUser(username, user, new UserRepository.UserCallback() {
                    @Override
                    public void onSuccess(User updated) {
                        Toast.makeText(StaffUserManagementActivity.this, "User updated.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(StaffUserManagementActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));

        dialog.show();
    }

    private void showDeleteDialog(UserRepository userRepository) {
        TextInputLayout layout = new TextInputLayout(this);
        TextInputEditText editText = new TextInputEditText(this);
        layout.setHint("Username");
        layout.addView(editText);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setView(layout)
                .setPositiveButton("Delete", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String username = textValue(editText);
            if (username.isEmpty()) {
                layout.setError("Username is required.");
                return;
            }
            layout.setError(null);
            userRepository.deleteUser(username, new UserRepository.CompletionCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(StaffUserManagementActivity.this, "User deleted.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(StaffUserManagementActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }));

        dialog.show();
    }

    private String textValue(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
