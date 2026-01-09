package com.example.myapplication.data.repository;

import java.util.List;

import com.example.myapplication.data.model.Reservation;

public interface ReservationRepository {
    List<Reservation> getReservationsByEmail(String email);

    List<Reservation> getReservationsByDate(String date);

    void deleteReservation(Reservation reservation);
}
