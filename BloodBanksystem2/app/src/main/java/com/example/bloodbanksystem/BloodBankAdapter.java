package com.example.bloodbanksystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.ViewHolder> {
    private List<BloodBank> bloodBankList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Interface for click listener
    public interface OnItemClickListener {
        void onItemClick(BloodBank bloodBank);
    }

    // Constructor to pass click listener
    public BloodBankAdapter(Context context, List<BloodBank> bloodBankList, OnItemClickListener listener) {
        this.context = context;
        this.bloodBankList = bloodBankList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blood_bank_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BloodBank bloodBank = bloodBankList.get(position);
        holder.bloodBankName.setText(bloodBank.getBloodBankName());
        holder.contactPerson.setText("Contact Person: " + bloodBank.getContactPerson());
        holder.phoneNumber.setText("Phone: " + bloodBank.getPhoneNumber());
        holder.email.setText("Email: " + bloodBank.getEmail());
        holder.address.setText("Address: " + bloodBank.getAddress());

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(bloodBank));
    }

    @Override
    public int getItemCount() {
        return bloodBankList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bloodBankName, contactPerson, phoneNumber, email, address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodBankName = itemView.findViewById(R.id.textBloodBankName);
            contactPerson = itemView.findViewById(R.id.textContactPerson);
            phoneNumber = itemView.findViewById(R.id.textPhoneNumber);
            email = itemView.findViewById(R.id.textEmail);
            address = itemView.findViewById(R.id.textAddress);
        }
    }
}
