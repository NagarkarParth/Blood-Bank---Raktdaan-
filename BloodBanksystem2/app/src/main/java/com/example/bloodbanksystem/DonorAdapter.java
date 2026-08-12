package com.example.bloodbanksystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {
    private final List<donor> donorList;
    private final Context context;

    // Constructor
    public DonorAdapter(List<donor> donorList, Context context) {
        this.donorList = donorList;
        this.context = context;
    }

    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donor, parent, false);
        return new DonorViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        donor donor = donorList.get(position);
        holder.tvName.setText("Name: " + donor.getName());
        holder.tvPhone.setText("Phone: " + donor.getPhone());
        holder.tvBloodGroup.setText("Blood Group: " + donor.getBloodGroup());
        holder.tvEmail.setText("Email: " + donor.getEmail());

        // Open dialer when clicking on phone number
        holder.tvPhone.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + donor.getPhone()));
            context.startActivity(dialIntent);
        });
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    // ViewHolder Class
    public static class DonorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvBloodGroup, tvEmail;

        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvBloodGroup = itemView.findViewById(R.id.tvBloodGroup);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }
}