package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.model.MenuItem;
import com.example.myapplication.data.repository.MenuRepository;
import com.example.myapplication.data.repository.MenuRepositoryImpl;
import com.example.myapplication.ui.adapters.MenuAdapter;
import com.example.myapplication.ui.viewmodel.MenuViewModel;

public class Menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        EdgeToEdge.enable(this);

        AppDatabase database = AppDatabase.getDatabase(this);
        MenuRepository menuRepository = new MenuRepositoryImpl(database.menuItemDao());
        MenuViewModel viewModel = new ViewModelProvider(
                this,
                new MenuViewModel.Factory(menuRepository)
        ).get(MenuViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.menuView);
        MenuAdapter menuAdapter = new MenuAdapter(menuItem -> {
            Intent intent = new Intent(this, MenuCloserLook.class);
            intent.putExtra("MenuID", menuItem.id);
            startActivity(intent);
        });
        recyclerView.setAdapter(menuAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ImageButton back = findViewById(R.id.imageButton12);
        back.setOnClickListener(v -> {
            Intent intentBack = new Intent(this, MainActivity.class);
            startActivity(intentBack);
        });

        List<Button> categories = Arrays.asList(
                findViewById(R.id.button_starters),
                findViewById(R.id.button_mains),
                findViewById(R.id.button_drinks),
                findViewById(R.id.button_desserts)
        );

        TextView title = findViewById(R.id.textView42);
        title.setText("");

        for (int i = 0; i < categories.size(); i++) {
            Button categoryButton = categories.get(i);
            categoryButton.setOnClickListener(v -> {
                String selectedCategory = categoryButton.getText().toString();
                title.setText(selectedCategory);
                List<MenuItem> menuItems = viewModel.getMenuByCategory(selectedCategory);
                menuAdapter.submitList(menuItems);

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
