package com.example.bloodbanksystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CampAdapter extends RecyclerView.Adapter<CampAdapter.CampViewHolder> {
    private Context context;
    private List<BloodCamp> campList;
    private OnItemClickListener listener; // Click listener for item clicks

    public CampAdapter(Context context, List<BloodCamp> campList, OnItemClickListener listener) {
        this.context = context;
        this.campList = campList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CampViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_camp, parent, false);
        return new CampViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampViewHolder holder, int position) {
        BloodCamp camp = campList.get(position);
        holder.tvName.setText(camp.getName());
        holder.tvDate.setText("Date: " + camp.getCampDate());
        holder.tvAddress.setText("Address: " + camp.getAddress());

        Glide.with(context)
                .load(camp.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.ivCampImage);

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(camp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return campList.size();
    }

    public static class CampViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvAddress;
        ImageView ivCampImage;

        public CampViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCampName);
            tvDate = itemView.findViewById(R.id.tvCampDate);
            tvAddress = itemView.findViewById(R.id.tvCampAddress);
            ivCampImage = itemView.findViewById(R.id.ivCampImage);
        }
    }

    // Interface for item clicks
    public interface OnItemClickListener {
        void onItemClick(BloodCamp camp);
    }
}
