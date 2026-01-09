package com.example.myapplication.data;

import java.util.List;

import com.example.myapplication.data.dao.MenuItemDao;
import com.example.myapplication.data.dao.ReservationsDao;
import com.example.myapplication.data.dao.TablesAvailabilityDao;
import com.example.myapplication.data.dao.TablesDao;
import com.example.myapplication.data.model.MenuItem;
import com.example.myapplication.data.model.Reservation;

public class CRUDintoMethods {
    private final MenuItemDao menuItemDao;
    private final TablesDao tablesDao;
    private final TablesAvailabilityDao tablesAvailabilityDao;
    private final ReservationsDao reservationsDao;

    public CRUDintoMethods(
            MenuItemDao menuItemDao,
            TablesDao tablesDao,
            TablesAvailabilityDao tablesAvailabilityDao,
            ReservationsDao reservationsDao
    ) {
        this.menuItemDao = menuItemDao;
        this.tablesDao = tablesDao;
        this.tablesAvailabilityDao = tablesAvailabilityDao;
        this.reservationsDao = reservationsDao;
    }

    public void insertMenuItem(MenuItem menuItem) {
        menuItemDao.insertMenuItem(menuItem);
    }

    public List<MenuItem> getMenuByCategory(String category) {
        return menuItemDao.getMenuByCategory(category);
    }

    public MenuItem getMenuByID(int id) {
        return menuItemDao.getMenuByID(id);
    }

    public void updateMenuItem(MenuItem menuItem) {
        menuItemDao.update(menuItem);
    }

    public void deleteMenuItem(MenuItem menuItem) {
        menuItemDao.delete(menuItem);
    }

    public long insertReservation(Reservation reservation) {
        return reservationsDao.insert(reservation);
    }

    public Reservation getReservationById(long id) {
        return reservationsDao.getReservationById(id);
    }

    public List<Reservation> getReservationsByEmail(String email) {
        return reservationsDao.getReservationsByEmail(email);
    }

    public void updateReservation(Reservation reservation) {
        reservationsDao.update(reservation);
    }

    public void deleteReservation(Reservation reservation) {
        reservationsDao.delete(reservation);
    }

    public List<Reservation> getReservationsByDate(String date) {
        return reservationsDao.getReservationsByDate(date);
    }

    public List<Reservation> getReservationsByName(String name) {
        return reservationsDao.getReservationsByName(name);
    }
}
