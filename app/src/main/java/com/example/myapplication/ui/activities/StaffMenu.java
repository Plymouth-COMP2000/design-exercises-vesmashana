package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.CRUDintoMethods;
import com.example.myapplication.data.model.MenuItem;
import com.example.myapplication.ui.adapters.StaffMenuAdapter;

public class StaffMenu extends AppCompatActivity {
    private StaffMenuAdapter menuItemAdapter;
    private AppDatabase database;
    private CRUDintoMethods repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_menu);
        database = AppDatabase.getDatabase(this);
        repository = new CRUDintoMethods(
                database.menuItemDao(),
                database.tablesDao(),
                database.tablesAvailabilityDao(),
                database.reservationsDao()
        );
        EdgeToEdge.enable(this);
        menuItemAdapter = new StaffMenuAdapter(menuItem -> {
            Intent intent3 = new Intent(this, EditItem.class);
            intent3.putExtra("MenuID", menuItem.id);
            startActivity(intent3);
        });
        RecyclerView recyclerView = findViewById(R.id.editMenuView);
        recyclerView.setAdapter(menuItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton back = findViewById(R.id.imageButton17);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffCentre.class);
            startActivity(intent);
        });
        ImageView addItem = findViewById(R.id.imageView17);
        addItem.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, AddItem.class);
            startActivity(intent1);
        });
        List<Button> categories = Arrays.asList(
                findViewById(R.id.button_starters),
                findViewById(R.id.button_mains),
                findViewById(R.id.button_drinks),
                findViewById(R.id.button_desserts)
        );

        for (Button categoryButton : categories) {
            categoryButton.setOnClickListener(v -> {
                String selectedCategory = categoryButton.getText().toString();
                List<MenuItem> menuItems = repository.getMenuByCategory(selectedCategory);
                menuItemAdapter.submitList(menuItems);
                for (Button button : categories) {
                    button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    button.setTextColor(Color.parseColor("#ff6750a4"));
                }
                categoryButton.setBackgroundColor(Color.parseColor("#ff6750a4"));
                categoryButton.setTextColor(Color.parseColor("#FFFFFF"));
            });
        }
    }
}
