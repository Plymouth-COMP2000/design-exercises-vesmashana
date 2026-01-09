package com.example.myapplication.core;

import android.app.Application;

import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.CRUDintoMethods;
import com.example.myapplication.data.remote.ApiClient;
import com.example.myapplication.data.repository.UserRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyRestaurantApplication extends Application {
    private AppDatabase database;
    private CRUDintoMethods repository;
    private UserRepository userRepository;
    private final Executor userExecutor = Executors.newSingleThreadExecutor();

    public AppDatabase getDatabase() {
        if (database == null) {
            database = AppDatabase.getDatabase(this);
        }
        return database;
    }

    public CRUDintoMethods getRepository() {
        if (repository == null) {
            AppDatabase database = getDatabase();
            repository = new CRUDintoMethods(
                    database.menuItemDao(),
                    database.tablesDao(),
                    database.tablesAvailabilityDao(),
                    database.reservationsDao()
            );
        }
        return repository;
    }

    public UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepository(
                    ApiClient.getUserService(),
                    getDatabase().userDao(),
                    userExecutor
            );
        }
        return userRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getUserRepository().initializeStudent();
    }
}
