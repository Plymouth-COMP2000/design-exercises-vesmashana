package com.example.myapplication.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import com.example.myapplication.data.model.Reservation;
import com.example.myapplication.data.repository.ReservationRepository;

// ViewModel for staff admin screens; the repository handles all reservation data access.
public class StaffAdminViewModel extends ViewModel {
    private final ReservationRepository reservationRepository;

    public StaffAdminViewModel(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getReservationsForDate(String date) {
        return reservationRepository.getReservationsByDate(date);
    }

    public void deleteReservation(Reservation reservation) {
        reservationRepository.deleteReservation(reservation);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final ReservationRepository reservationRepository;

        public Factory(ReservationRepository reservationRepository) {
            this.reservationRepository = reservationRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(StaffAdminViewModel.class)) {
                return (T) new StaffAdminViewModel(reservationRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
