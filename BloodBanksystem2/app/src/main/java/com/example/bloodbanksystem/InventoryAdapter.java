package com.example.bloodbanksystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private List<InventoryModel> inventoryList;

    public InventoryAdapter(List<InventoryModel> inventoryList) {
        this.inventoryList = inventoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryModel item = inventoryList.get(position);
        holder.bloodGroup.setText("Blood Group: " + item.getBloodGroup());
        holder.quantity.setText("Quantity: " + item.getQuantity());
        holder.dateOfCollection.setText("Collected On: " + item.getDateOfCollection());
        holder.dateOfExpiration.setText("Expires On: " + item.getDateOfExpiration());
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bloodGroup, quantity, dateOfCollection, dateOfExpiration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodGroup = itemView.findViewById(R.id.tvBloodGroup);
            quantity = itemView.findViewById(R.id.tvQuantity);
            dateOfCollection = itemView.findViewById(R.id.etDateOfCollection);
            dateOfExpiration = itemView.findViewById(R.id.etDateOfExpiration);
        }
    }
}
