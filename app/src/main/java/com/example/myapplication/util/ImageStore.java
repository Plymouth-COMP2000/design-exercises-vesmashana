package com.example.myapplication.util;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

public class ImageStore {
    private final Context context;

    public ImageStore(Context context) {
        this.context = context;
    }

    public String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                return null;
            }

            File storageDir = new File(context.getFilesDir(), "menu_images");
            if (!storageDir.exists() && !storageDir.mkdirs()) {
                return null;
            }
            String fileName = UUID.randomUUID() + ".jpg";
            File imageFile = new File(storageDir, fileName);

            try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                byte[] buffer = new byte[4096];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
            } finally {
                inputStream.close();
            }

            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
