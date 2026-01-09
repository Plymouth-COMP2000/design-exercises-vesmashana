package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.example.myapplication.R;
import com.example.myapplication.ui.fragments.FragmentMonday;
import com.example.myapplication.ui.viewmodel.DatesViewModel;

public class StaffReservations extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatesViewModel viewModel = new ViewModelProvider(this).get(DatesViewModel.class);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_reservations);
        ImageButton back = findViewById(R.id.imageButton13);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffCentre.class);
            startActivity(intent);
        });
        ImageButton searchByName = findViewById(R.id.imageButton14);
        searchByName.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, SearchByName.class);
            startActivity(intent1);
        });
        ImageButton staffLog = findViewById(R.id.imageButton15);
        staffLog.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, ReservationsLog.class);
            startActivity(intent2);
        });

        ImageButton checkNew = findViewById(R.id.imageButton11);
        checkNew.setOnClickListener(v -> newReservations());
        TabLayout daysOfWeek = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        DaysPagerAdapter adapter = new DaysPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(daysOfWeek, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Mo");
                    break;
                case 1:
                    tab.setText("Tue");
                    break;
                case 2:
                    tab.setText("Wed");
                    break;
                case 3:
                    tab.setText("Thu");
                    break;
                case 4:
                    tab.setText("Fri");
                    break;
                case 5:
                    tab.setText("Sat");
                    break;
                case 6:
                    tab.setText("Sun");
                    break;
                case 7:
                    tab.setText("Cal");
                    break;
                default:
                    break;
            }
        }).attach();

        daysOfWeek.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                java.util.List<String> weekDates = viewModel.getWeekDates().getValue();
                if (tab != null && weekDates != null && tab.getPosition() < weekDates.size()) {
                    String selectedDateForTab = weekDates.get(tab.getPosition());
                    Log.d("TabSelection", "Tab at position " + tab.getPosition() + " selected. Date: " + selectedDateForTab);
                    viewModel.selectDate(selectedDateForTab);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewModel.getSelectedDate().observe(this, newDate -> {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            java.util.Date date;
            try {
                date = formatter.parse(newDate);
            } catch (Exception e) {
                date = null;
            }
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                int tabIndex;
                switch (dayOfWeek) {
                    case Calendar.MONDAY:
                        tabIndex = 0;
                        break;
                    case Calendar.TUESDAY:
                        tabIndex = 1;
                        break;
                    case Calendar.WEDNESDAY:
                        tabIndex = 2;
                        break;
                    case Calendar.THURSDAY:
                        tabIndex = 3;
                        break;
                    case Calendar.FRIDAY:
                        tabIndex = 4;
                        break;
                    case Calendar.SATURDAY:
                        tabIndex = 5;
                        break;
                    case Calendar.SUNDAY:
                        tabIndex = 6;
                        break;
                    default:
                        tabIndex = -1;
                        break;
                }
                if (tabIndex != -1 && viewPager.getCurrentItem() != tabIndex) {
                    viewPager.setCurrentItem(tabIndex);
                }
            }
        });
    }

    private void newReservations() {
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.popup_newres, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        ImageButton closeButton = dialogView.findViewById(R.id.imageButton20);
        ImageButton backButton = dialogView.findViewById(R.id.imageButton21);
        android.widget.Button placeholderButton = dialogView.findViewById(R.id.button28);

        androidx.constraintlayout.widget.Group viewAllGroup = dialogView.findViewById(R.id.ViewAll);
        androidx.constraintlayout.widget.Group viewOneGroup = dialogView.findViewById(R.id.ViewOne);
        viewAllGroup.setVisibility(android.view.View.VISIBLE);
        viewOneGroup.setVisibility(android.view.View.GONE);
        dialog.show();
        closeButton.setOnClickListener(v -> dialog.dismiss());
        placeholderButton.setOnClickListener(v -> {
            viewAllGroup.setVisibility(android.view.View.GONE);
            viewOneGroup.setVisibility(android.view.View.VISIBLE);
        });
        backButton.setOnClickListener(v -> {
            viewAllGroup.setVisibility(android.view.View.VISIBLE);
            viewOneGroup.setVisibility(android.view.View.GONE);
        });
    }

    public static class DaysPagerAdapter extends FragmentStateAdapter {
        public DaysPagerAdapter(FragmentActivity activity) {
            super(activity);
        }

        @Override
        public int getItemCount() {
            return 8;
        }

        @Override
        public Fragment createFragment(int position) {
            if (position == 7) {
                return StaffCalendar.newInstance();
            }
            return FragmentMonday.newInstance(position);
        }
    }
}
