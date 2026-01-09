package com.example.myapplication.data.repository;

import java.util.List;

import com.example.myapplication.data.model.MenuItem;

public interface MenuRepository {
    List<MenuItem> getMenuByCategory(String category);

    MenuItem getMenuById(int id);
}
