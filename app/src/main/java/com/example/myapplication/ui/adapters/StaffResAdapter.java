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
import com.example.myapplication.data.model.Reservation;

public class StaffResAdapter extends ListAdapter<Reservation, StaffResAdapter.ReservationViewHolder> {
    public interface OnDeleteClicked {
        void onDeleteClicked(Reservation reservation);
    }

    private final OnDeleteClicked onDeleteClicked;

    public StaffResAdapter(OnDeleteClicked onDeleteClicked) {
        super(new ReservationDiffCallback());
        this.onDeleteClicked = onDeleteClicked;
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final Button deleteButton;
        private final TextView nameView;
        private final TextView numberOfPeopleView;
        private final TextView dateView;
        private final TextView timeView;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.button18);
            nameView = itemView.findViewById(R.id.textView86);
            numberOfPeopleView = itemView.findViewById(R.id.textView88);
            dateView = itemView.findViewById(R.id.textView91);
            timeView = itemView.findViewById(R.id.textView92);
        }

        public void bind(Reservation reservation, OnDeleteClicked onDeleteClicked) {
            android.util.Log.d("FragmentMonday", "Binding reservation: reservation.name = " + reservation.name);
            nameView.setText(reservation.name);
            numberOfPeopleView.setText(String.valueOf(reservation.numberOfPeople));
            dateView.setText(reservation.date);
            timeView.setText(reservation.time);
            deleteButton.setOnClickListener(v -> onDeleteClicked.onDeleteClicked(reservation));
        }
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.util.Log.d("FragmentMonday", "onCreate inflation is running!");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.staffres_dateviewlayout, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = getItem(position);
        holder.bind(reservation, onDeleteClicked);
    }

    public static class ReservationDiffCallback extends DiffUtil.ItemCallback<Reservation> {
        @Override
        public boolean areItemsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
            return oldItem.equals(newItem);
        }
    }
}
