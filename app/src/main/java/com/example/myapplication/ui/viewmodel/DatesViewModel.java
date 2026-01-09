package com.example.myapplication.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatesViewModel extends ViewModel {
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<Integer> dayOfWeek = new MutableLiveData<>();
    private final MutableLiveData<List<String>> weekDates = new MutableLiveData<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public DatesViewModel() {
        Calendar today = Calendar.getInstance();
        selectDate(dateFormat.format(today.getTime()));
    }

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public LiveData<Integer> getDayOfWeek() {
        return dayOfWeek;
    }

    public LiveData<List<String>> getWeekDates() {
        return weekDates;
    }

    public void selectDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String formattedDate = dateFormat.format(calendar.getTime());
        selectDate(formattedDate);
    }

    public void selectDate(String dateString) {
        java.util.Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            android.util.Log.e("DatesViewModel", "Failed to parse date: " + dateString, e);
            return;
        }
        if (date == null) {
            return;
        }

        selectedDate.setValue(dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfWeekInt = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                ? 7
                : calendar.get(Calendar.DAY_OF_WEEK) - 1;
        dayOfWeek.setValue(dayOfWeekInt);

        updateWeekDates(calendar);
    }

    private void updateWeekDates(Calendar calendar) {
        Calendar tempCal = (Calendar) calendar.clone();
        tempCal.setFirstDayOfWeek(Calendar.MONDAY);
        tempCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dates.add(dateFormat.format(tempCal.getTime()));
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        weekDates.setValue(dates);
        android.util.Log.d("DatesViewModel", "Updated week dates: " + weekDates.getValue());
    }
}
