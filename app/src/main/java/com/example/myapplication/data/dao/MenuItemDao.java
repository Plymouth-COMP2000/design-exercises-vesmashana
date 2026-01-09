package com.example.myapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.myapplication.data.model.MenuItem;

@Dao
public interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMenuItem(MenuItem menuItem);

    @Query("SELECT * FROM menu_items WHERE category = :category")
    List<MenuItem> getMenuByCategory(String category);

    @Query("SELECT * FROM menu_items WHERE id = :id")
    MenuItem getMenuByID(int id);

    @Update
    void update(MenuItem menuItem);

    @Delete
    void delete(MenuItem menuItem);
}
