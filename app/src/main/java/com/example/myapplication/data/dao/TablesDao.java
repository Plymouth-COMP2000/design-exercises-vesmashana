package com.example.myapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.example.myapplication.data.model.Tables;

@Dao
public interface TablesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Tables table);

    @Query("SELECT * FROM Tables ORDER BY tableNo ASC")
    List<Tables> getAllTables();

    @Query("SELECT * FROM Tables")
    List<Tables> getAllTablesList();
}
