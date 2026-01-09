package com.example.myapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.myapplication.data.model.Reservation;

@Dao
public interface ReservationsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Reservation reservation);

    @Query("SELECT * FROM Reservations ORDER BY date, time ASC")
    List<Reservation> getAllReservations();

    @Query("SELECT * FROM Reservations WHERE email = :email ORDER BY date, time ASC")
    List<Reservation> getReservationsByEmail(String email);

    @Query("SELECT * FROM Reservations WHERE date = :date ORDER BY time ASC")
    List<Reservation> getReservationsByDate(String date);

    @Query("SELECT * FROM Reservations WHERE id = :id")
    Reservation getReservationById(long id);

    @Query("SELECT * FROM Reservations WHERE name = :name ORDER BY date, time ASC")
    List<Reservation> getReservationsByName(String name);

    @Update
    void update(Reservation reservation);

    @Delete
    void delete(Reservation reservation);
}
