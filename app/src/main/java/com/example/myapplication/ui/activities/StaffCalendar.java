package com.example.myapplication.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import com.example.myapplication.ui.viewmodel.DatesViewModel;

public class StaffCalendar extends Fragment {
    private DatesViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(DatesViewModel.class);
        CalendarView calendar = view.findViewById(R.id.calendarView2);
        calendar.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> viewModel.selectDate(year, month, dayOfMonth));
    }

    public static StaffCalendar newInstance() {
        return new StaffCalendar();
    }
}
