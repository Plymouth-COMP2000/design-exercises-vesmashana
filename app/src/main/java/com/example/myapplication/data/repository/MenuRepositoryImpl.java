package com.example.myapplication.data.repository;

import java.util.List;

import com.example.myapplication.data.dao.MenuItemDao;
import com.example.myapplication.data.model.MenuItem;

// Repository implementation for menu data access in the MVVM pattern.
public class MenuRepositoryImpl implements MenuRepository {
    private final MenuItemDao menuItemDao;

    public MenuRepositoryImpl(MenuItemDao menuItemDao) {
        this.menuItemDao = menuItemDao;
    }

    @Override
    public List<MenuItem> getMenuByCategory(String category) {
        return menuItemDao.getMenuByCategory(category);
    }

    @Override
    public MenuItem getMenuById(int id) {
        return menuItemDao.getMenuByID(id);
    }
}
