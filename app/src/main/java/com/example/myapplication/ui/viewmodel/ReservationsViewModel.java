package com.example.myapplication.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import com.example.myapplication.data.model.Reservation;
import com.example.myapplication.data.repository.ReservationRepository;

// ViewModel for the reservations screen; keeps data access in repositories per MVVM.
public class ReservationsViewModel extends ViewModel {
    private final ReservationRepository reservationRepository;

    public ReservationsViewModel(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation findFirstReservationByEmail(String email) {
        List<Reservation> reservations = reservationRepository.getReservationsByEmail(email);
        return reservations != null && !reservations.isEmpty() ? reservations.get(0) : null;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final ReservationRepository reservationRepository;

        public Factory(ReservationRepository reservationRepository) {
            this.reservationRepository = reservationRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ReservationsViewModel.class)) {
                return (T) new ReservationsViewModel(reservationRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
