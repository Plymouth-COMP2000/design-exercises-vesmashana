package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.CRUDintoMethods;
import com.example.myapplication.data.model.MenuItem;
import com.example.myapplication.util.ImageStore;

public class EditItem extends AppCompatActivity {
    private String savedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_item);
        Button addImage = findViewById(R.id.button23);

        addImage.setVisibility(View.GONE);
        androidx.constraintlayout.widget.Group editGroup = findViewById(R.id.editGroup);
        editGroup.setVisibility(View.VISIBLE);

        AppDatabase database = AppDatabase.getDatabase(this);
        CRUDintoMethods repository = new CRUDintoMethods(
                database.menuItemDao(),
                database.tablesDao(),
                database.tablesAvailabilityDao(),
                database.reservationsDao()
        );

        int menuItemId = getIntent().getIntExtra("MenuID", -1);
        TextInputEditText nameField = findViewById(R.id.name);
        TextInputEditText descField = findViewById(R.id.description);
        TextInputEditText priceField = findViewById(R.id.price);
        ImageView image = findViewById(R.id.imageView6);

        MenuItem menuItem = repository.getMenuByID(menuItemId);
        if (menuItem != null) {
            if (menuItem.imagePath != null && !menuItem.imagePath.isEmpty()) {
                File imgFile = new File(menuItem.imagePath);
                if (imgFile.exists()) {
                    image.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                }
            }
            nameField.setHint(String.valueOf(menuItem.name));
            descField.setHint(String.valueOf(menuItem.description));
            priceField.setHint(String.valueOf(menuItem.price));
        }

        ImageButton back = findViewById(R.id.imageButton10);
        back.setOnClickListener(v -> showConfirm());

        ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        ImageStore imageManager = new ImageStore(this);
                        savedImagePath = imageManager.saveImageToInternalStorage(uri);
                        image.setImageURI(uri);
                    }
                }
        );
        Button updateImage = findViewById(R.id.button30);
        updateImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        List<Button> categories = Arrays.asList(
                findViewById(R.id.button_starters),
                findViewById(R.id.button_mains),
                findViewById(R.id.button_drinks),
                findViewById(R.id.button_desserts)
        );
        final String[] selectedCategory = {""};

        for (Button categoryButton : categories) {
            categoryButton.setOnClickListener(v -> {
                selectedCategory[0] = categoryButton.getText().toString();
                for (Button button : categories) {
                    button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    button.setTextColor(Color.parseColor("#ff6750a4"));
                }
                categoryButton.setBackgroundColor(Color.parseColor("#ff6750a4"));
                categoryButton.setTextColor(Color.parseColor("#FFFFFF"));
            });
        }
        Button createItem = findViewById(R.id.button24);
        createItem.setOnClickListener(v -> {
            String name = nameField.getText() != null ? nameField.getText().toString() : "";
            String desc = descField.getText() != null ? descField.getText().toString() : "";
            String price = priceField.getText() != null ? priceField.getText().toString() : "";

            TextView warning1 = findViewById(R.id.textView79);
            TextView warning2 = findViewById(R.id.textView80);
            TextView warning3 = findViewById(R.id.textView81);
            TextView warning4 = findViewById(R.id.textView82);
            TextView warning5 = findViewById(R.id.textView83);

            MenuItem originalItem = repository.getMenuByID(menuItemId);
            if (originalItem != null) {
                String updatedName = !name.isEmpty() ? name : originalItem.name;
                String updatedDescription = !desc.isEmpty() ? desc : originalItem.description;
                double updatedPrice = !price.isEmpty() ? Double.parseDouble(price) : originalItem.price;
                String updatedImage = savedImagePath != null ? savedImagePath : originalItem.imagePath;
                String updatedCategory = !selectedCategory[0].isEmpty() ? selectedCategory[0] : originalItem.category;

                MenuItem updatedMenuItem = new MenuItem(
                        originalItem.id,
                        updatedName,
                        updatedDescription,
                        updatedPrice,
                        updatedImage,
                        updatedCategory
                );
                repository.updateMenuItem(updatedMenuItem);
                finish();
            }
        });
    }

    private void showConfirm() {
        View dialogView = getLayoutInflater().inflate(R.layout.popup_keepchanges, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        Button continueButton = dialogView.findViewById(R.id.button27);
        Button discardButton = dialogView.findViewById(R.id.button26);

        dialog.show();
        continueButton.setOnClickListener(v -> dialog.dismiss());
        discardButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffMenu.class);
            startActivity(intent);
            finish();
            dialog.dismiss();
        });
    }
}
