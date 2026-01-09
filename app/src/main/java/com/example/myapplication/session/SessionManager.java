package com.example.myapplication.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.data.model.User;

public class SessionManager {
    private static final String PREFS_NAME = "user_session";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERTYPE = "usertype";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(User user) {
        preferences.edit()
                .putString(KEY_USERNAME, user.username)
                .putString(KEY_USERTYPE, user.usertype)
                .putString(KEY_FIRSTNAME, user.firstname)
                .putString(KEY_LASTNAME, user.lastname)
                .apply();
    }

    public boolean isLoggedIn() {
        return getUsername() != null && !getUsername().isEmpty();
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, "");
    }

    public String getUsertype() {
        return preferences.getString(KEY_USERTYPE, "");
    }

    public String getFirstname() {
        return preferences.getString(KEY_FIRSTNAME, "");
    }

    public String getLastname() {
        return preferences.getString(KEY_LASTNAME, "");
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
