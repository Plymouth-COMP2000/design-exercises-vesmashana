package com.example.myapplication.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "tablesAvailability", primaryKeys = {"tableNo", "date", "time"})
public class TablesAvailability {
    public int tableNo;
    @NonNull
    public String date;
    @NonNull
    public String time;
    public boolean available;

    public TablesAvailability(int tableNo, String date, String time, boolean available) {
        this.tableNo = tableNo;
        this.date = date;
        this.time = time;
        this.available = available;
    }

    @Ignore
    public TablesAvailability(int tableNo, String date, String time) {
        this(tableNo, date, time, true);
    }
}
