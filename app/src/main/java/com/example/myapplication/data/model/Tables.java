package com.example.myapplication.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tables")
public class Tables {
    @PrimaryKey(autoGenerate = true)
    public int tableNo;
    public boolean fitsPreference1;
    public boolean fitsPreference2;

    public Tables(int tableNo, boolean fitsPreference1, boolean fitsPreference2) {
        this.tableNo = tableNo;
        this.fitsPreference1 = fitsPreference1;
        this.fitsPreference2 = fitsPreference2;
    }

    @Ignore
    public Tables(boolean fitsPreference1, boolean fitsPreference2) {
        this(0, fitsPreference1, fitsPreference2);
    }
}
