package com.example.myapplication.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "StaffAccount")
public class StaffAccounts {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String username;
    public String password;
    public String email;

    public StaffAccounts(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Ignore
    public StaffAccounts(String username, String password, String email) {
        this(0, username, password, email);
    }
}
