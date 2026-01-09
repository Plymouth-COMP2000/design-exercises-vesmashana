package com.example.myapplication.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.myapplication.data.dao.MenuItemDao;
import com.example.myapplication.data.dao.ReservationsDao;
import com.example.myapplication.data.dao.TablesAvailabilityDao;
import com.example.myapplication.data.dao.TablesDao;
import com.example.myapplication.data.local.UserDao;
import com.example.myapplication.data.model.MenuItem;
import com.example.myapplication.data.model.Reservation;
import com.example.myapplication.data.model.Tables;
import com.example.myapplication.data.model.TablesAvailability;
import com.example.myapplication.data.model.User;

@Database(entities = {MenuItem.class, Tables.class, TablesAvailability.class, Reservation.class, User.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MenuItemDao menuItemDao();

    public abstract TablesDao tablesDao();

    public abstract TablesAvailabilityDao tablesAvailabilityDao();

    public abstract ReservationsDao reservationsDao();

    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;
    private static final ExecutorService DATABASE_WRITE_EXECUTOR = Executors.newFixedThreadPool(2);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "restaurant_database")
                            .allowMainThreadQueries()
                            .addCallback(new CreateTables())
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class CreateTables extends RoomDatabase.Callback {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            DATABASE_WRITE_EXECUTOR.execute(() -> {
                if (INSTANCE == null) {
                    return;
                }
                TablesDao tablesDao = INSTANCE.tablesDao();
                for (int i = 1; i <= 15; i++) {
                    boolean fitsPreference1 = i >= 1 && i <= 5 || i >= 8 && i <= 9 || i >= 11 && i <= 15;
                    boolean fitsPreference2 = i >= 8 && i <= 10;
                    tablesDao.insert(new Tables(fitsPreference1, fitsPreference2));
                }
            });
        }
    }
}
