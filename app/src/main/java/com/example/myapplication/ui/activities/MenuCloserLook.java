package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.model.MenuItem;
import com.example.myapplication.data.repository.MenuRepository;
import com.example.myapplication.data.repository.MenuRepositoryImpl;
import com.example.myapplication.ui.viewmodel.MenuViewModel;

public class MenuCloserLook extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menucloserlook);
        AppDatabase database = AppDatabase.getDatabase(this);
        MenuRepository menuRepository = new MenuRepositoryImpl(database.menuItemDao());
        MenuViewModel viewModel = new ViewModelProvider(
                this,
                new MenuViewModel.Factory(menuRepository)
        ).get(MenuViewModel.class);
        int menuItemId = getIntent().getIntExtra("MenuID", -1);

        TextView name = findViewById(R.id.textView55);
        TextView desc = findViewById(R.id.textView57);
        TextView price = findViewById(R.id.textView56);
        ImageView image = findViewById(R.id.imageView13);

        MenuItem menuItem = viewModel.getMenuById(menuItemId);
        if (menuItem != null) {
            String imagePath = menuItem.imagePath;
            if (imagePath != null && !imagePath.isEmpty()) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    image.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                }
            }
            name.setText(menuItem.name);
            desc.setText(menuItem.description);
            price.setText(String.valueOf(menuItem.price));
        }

        ImageButton back = findViewById(R.id.imageButton7);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
        });
    }
}
