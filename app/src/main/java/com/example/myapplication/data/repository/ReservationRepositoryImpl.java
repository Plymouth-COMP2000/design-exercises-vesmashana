package com.example.myapplication.data.repository;

import java.util.List;

import com.example.myapplication.data.dao.ReservationsDao;
import com.example.myapplication.data.model.Reservation;

// Repository implementation for reservation data access in the MVVM pattern.
public class ReservationRepositoryImpl implements ReservationRepository {
    private final ReservationsDao reservationsDao;

    public ReservationRepositoryImpl(ReservationsDao reservationsDao) {
        this.reservationsDao = reservationsDao;
    }

    @Override
    public List<Reservation> getReservationsByEmail(String email) {
        return reservationsDao.getReservationsByEmail(email);
    }

    @Override
    public List<Reservation> getReservationsByDate(String date) {
        return reservationsDao.getReservationsByDate(date);
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        reservationsDao.delete(reservation);
    }
}
