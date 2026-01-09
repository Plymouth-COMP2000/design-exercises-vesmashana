package com.example.myapplication.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Reservations")
public class Reservation {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String email;
    public String date;
    public String time;
    public int numberOfPeople;
    public boolean pref1;
    public boolean pref2;
    public int tableNo;

    public Reservation(int id, String name, String email, String date, String time, int numberOfPeople, boolean pref1, boolean pref2, int tableNo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
        this.numberOfPeople = numberOfPeople;
        this.pref1 = pref1;
        this.pref2 = pref2;
        this.tableNo = tableNo;
    }

    @Ignore
    public Reservation(String name, String email, String date, String time, int numberOfPeople, boolean pref1, boolean pref2) {
        this(0, name, email, date, time, numberOfPeople, pref1, pref2, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return id == that.id
                && numberOfPeople == that.numberOfPeople
                && pref1 == that.pref1
                && pref2 == that.pref2
                && tableNo == that.tableNo
                && java.util.Objects.equals(name, that.name)
                && java.util.Objects.equals(email, that.email)
                && java.util.Objects.equals(date, that.date)
                && java.util.Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, email, date, time, numberOfPeople, pref1, pref2, tableNo);
    }
}
