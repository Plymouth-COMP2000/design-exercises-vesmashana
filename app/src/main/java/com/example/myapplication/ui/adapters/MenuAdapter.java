package com.example.myapplication.ui.adapters;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import com.example.myapplication.R;
import com.example.myapplication.data.model.MenuItem;

public class MenuAdapter extends ListAdapter<MenuItem, MenuAdapter.ViewHolder> {
    public interface OnCloserClicked {
        void onCloserClicked(MenuItem menuItem);
    }

    private final OnCloserClicked onCloserClicked;

    public MenuAdapter(OnCloserClicked onCloserClicked) {
        super(new MenuAdapterCallback());
        this.onCloserClicked = onCloserClicked;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView description;
        private final TextView priceView;
        private final ImageView image;
        private final ImageView backButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.textView55);
            description = itemView.findViewById(R.id.textView57);
            priceView = itemView.findViewById(R.id.textView56);
            image = itemView.findViewById(R.id.imageView13);
            backButton = itemView.findViewById(R.id.imageButton7);
            backButton.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
        }

        public void bind(MenuItem menuItem, OnCloserClicked onCloserClicked) {
            image.setOnClickListener(v -> onCloserClicked.onCloserClicked(menuItem));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.util.Log.d("FragmentMonday", "onCreate inflation is running!");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.menucloserlook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = getItem(position);
        holder.nameView.setText(menuItem.name);
        holder.priceView.setText(String.valueOf(menuItem.price));
        File imgFile = new File(menuItem.imagePath);
        if (imgFile.exists()) {
            holder.image.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        }
        holder.bind(menuItem, onCloserClicked);
    }

    public static class MenuAdapterCallback extends DiffUtil.ItemCallback<MenuItem> {
        @Override
        public boolean areItemsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            return oldItem.equals(newItem);
        }
    }
}
