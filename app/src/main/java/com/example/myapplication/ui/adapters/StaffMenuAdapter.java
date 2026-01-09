package com.example.myapplication.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.model.MenuItem;

public class StaffMenuAdapter extends ListAdapter<MenuItem, StaffMenuAdapter.MenuViewHolder> {
    public interface OnEditClicked {
        void onEditClicked(MenuItem menuItem);
    }

    private final OnEditClicked onEditClicked;

    public StaffMenuAdapter(OnEditClicked onEditClicked) {
        super(new MenuItemDiffCallback());
        this.onEditClicked = onEditClicked;
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        private final Button editButton;
        private final TextView nameView;
        private final TextView priceView;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            editButton = itemView.findViewById(R.id.button29);
            nameView = itemView.findViewById(R.id.textView96);
            priceView = itemView.findViewById(R.id.textView98);
        }

        public void bind(MenuItem menuItem, OnEditClicked onEditClicked) {
            nameView.setText(menuItem.name);
            priceView.setText(String.format("£%.2f", menuItem.price));
            editButton.setOnClickListener(v -> onEditClicked.onEditClicked(menuItem));
        }
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.util.Log.d("FragmentMonday", "onCreate inflation is running!");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.staffmenuonerow, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = getItem(position);
        holder.bind(menuItem, onEditClicked);
    }

    public static class MenuItemDiffCallback extends DiffUtil.ItemCallback<MenuItem> {
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
