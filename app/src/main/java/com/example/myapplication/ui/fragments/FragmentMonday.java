package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.CRUDintoMethods;
import com.example.myapplication.data.model.Reservation;
import com.example.myapplication.ui.adapters.StaffResAdapter;
import com.example.myapplication.ui.viewmodel.DatesViewModel;

public class FragmentMonday extends Fragment {
    private static final String ARG_POSITION = "arg_position";

    private DatesViewModel viewModel;
    private CRUDintoMethods repository;
    private StaffResAdapter staffResAdapter;
    private int dayIndex = 0;

    public static FragmentMonday newInstance(int position) {
        FragmentMonday fragment = new FragmentMonday();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase database = AppDatabase.getDatabase(requireContext());
        repository = new CRUDintoMethods(
                database.menuItemDao(),
                database.tablesDao(),
                database.tablesAvailabilityDao(),
                database.reservationsDao()
        );
        viewModel = new ViewModelProvider(requireActivity()).get(DatesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monday, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayIndex = getArguments() != null ? getArguments().getInt(ARG_POSITION) : 0;
        staffResAdapter = new StaffResAdapter(this::deleteReservation);

        RecyclerView recyclerView = view.findViewById(R.id.reservationsRecyclerView);
        recyclerView.setAdapter(staffResAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.getWeekDates().observe(getViewLifecycleOwner(), weekDates -> {
            if (weekDates != null && dayIndex < weekDates.size()) {
                String selectedDate = weekDates.get(dayIndex);
                android.util.Log.d("FragmentMonday", "Selected date: " + selectedDate);
                List<Reservation> reservations = repository.getReservationsByDate(selectedDate);
                if (reservations != null) {
                    android.util.Log.d("FragmentMonday", "Database returned " + reservations.size() + " reservations for " + selectedDate);
                    staffResAdapter.submitList(reservations);
                }
            }
        });
    }

    private void deleteReservation(Reservation reservation) {
        View dialogView = requireActivity().getLayoutInflater().inflate(R.layout.popup_deleteres, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        ImageButton closeButton = dialogView.findViewById(R.id.imageButton22);
        Button deleteButton = dialogView.findViewById(R.id.button17);
        Button noButton = dialogView.findViewById(R.id.button25);
        TextView nameView = dialogView.findViewById(R.id.textView85);
        TextView dateView = dialogView.findViewById(R.id.textView45);
        TextView timeView = dialogView.findViewById(R.id.textView90);
        nameView.setText(reservation.name);
        dateView.setText(reservation.date);
        timeView.setText(reservation.time);

        dialog.show();
        closeButton.setOnClickListener(v -> dialog.dismiss());
        deleteButton.setOnClickListener(v -> {
            List<Reservation> currentList = new ArrayList<>(staffResAdapter.getCurrentList());
            currentList.remove(reservation);
            staffResAdapter.submitList(currentList);
            repository.deleteReservation(reservation);
            dialog.dismiss();
        });
        noButton.setOnClickListener(v -> dialog.dismiss());
    }
}
