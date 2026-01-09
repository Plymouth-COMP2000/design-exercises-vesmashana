package com.example.myapplication.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "menu_items")
public class MenuItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public double price;
    public String imagePath;
    public String category;

    public MenuItem(int id, String name, String description, double price, String imagePath, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
        this.category = category;
    }

    @Ignore
    public MenuItem(String name, String description, double price, String imagePath, String category) {
        this(0, name, description, price, imagePath, category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuItem menuItem = (MenuItem) o;
        return id == menuItem.id
                && Double.compare(menuItem.price, price) == 0
                && java.util.Objects.equals(name, menuItem.name)
                && java.util.Objects.equals(description, menuItem.description)
                && java.util.Objects.equals(imagePath, menuItem.imagePath)
                && java.util.Objects.equals(category, menuItem.category);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, description, price, imagePath, category);
    }
}
