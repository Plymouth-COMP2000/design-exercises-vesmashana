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

public class AddItem extends AppCompatActivity {
    private String savedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_item);
        androidx.constraintlayout.widget.Group editGroup = findViewById(R.id.editGroup);
        editGroup.setVisibility(View.GONE);
        TextInputEditText nameField = findViewById(R.id.name);
        TextInputEditText descField = findViewById(R.id.description);
        TextInputEditText priceField = findViewById(R.id.price);
        ImageView imageView = findViewById(R.id.imageView6);
        nameField.setHint("Name");
        descField.setHint("Description");
        priceField.setHint("Price");

        AppDatabase database = AppDatabase.getDatabase(this);
        CRUDintoMethods repository = new CRUDintoMethods(
                database.menuItemDao(),
                database.tablesDao(),
                database.tablesAvailabilityDao(),
                database.reservationsDao()
        );

        ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        ImageStore imageManager = new ImageStore(this);
                        savedImagePath = imageManager.saveImageToInternalStorage(uri);
                        imageView.setImageURI(uri);
                        imageView.setVisibility(View.GONE);
                        editGroup.setVisibility(View.VISIBLE);
                        File imgFile = new File(savedImagePath);
                        if (imgFile.exists()) {
                            imageView.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                        }
                    }
                }
        );

        ImageButton back = findViewById(R.id.imageButton10);
        back.setOnClickListener(v -> showConfirm());
        Button addImage = findViewById(R.id.button23);
        addImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
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
            warning1.setText("");
            warning2.setText("");
            warning3.setText("");
            warning4.setText("");
            warning5.setText("");
            if (savedImagePath == null) {
                warning2.setText("*This is a required field");
            } else if (name.isEmpty()) {
                warning1.setText("*This is a required field");
            } else if (desc.isEmpty()) {
                warning3.setText("*This is a required field");
            } else if (price.isEmpty()) {
                warning4.setText("*This is a required field");
            } else if (selectedCategory[0].isEmpty()) {
                warning5.setText("*This is a required field");
            } else {
                MenuItem menuItem = new MenuItem(
                        name,
                        desc,
                        Double.parseDouble(price),
                        savedImagePath,
                        selectedCategory[0]
                );
                repository.insertMenuItem(menuItem);
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
