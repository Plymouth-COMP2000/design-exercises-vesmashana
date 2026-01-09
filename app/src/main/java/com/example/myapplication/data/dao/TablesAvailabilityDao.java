package com.example.myapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.example.myapplication.data.model.TablesAvailability;

@Dao
public interface TablesAvailabilityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TablesAvailability> availability);

    @Query("SELECT * FROM tablesAvailability WHERE date = :date AND time = :time AND available = 1")
    List<TablesAvailability> getAvailableTables(String date, String time);

    @Query("SELECT COUNT(*) FROM tablesAvailability WHERE date = :date AND time = :time")
    int countAvailabilitiesForTimeslot(String date, String time);
}
