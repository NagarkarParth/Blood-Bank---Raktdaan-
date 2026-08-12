package com.example.bloodbanksystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.Button;

public class DonorListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonorAdapter donorAdapter;
    private List<donor> donorList;
    private DatabaseReference databaseReference;
    private String selectedBloodGroup;
    private Button btnRequestBlood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewDonors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize List and Adapter
        donorList = new ArrayList<>();
        donorAdapter = new DonorAdapter(donorList, this);
        recyclerView.setAdapter(donorAdapter);

        // Initialize Button
        btnRequestBlood = findViewById(R.id.btnRequestBlood);

        // Firebase Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("donors");

        // Get selected blood group from intent
        selectedBloodGroup = getIntent().getStringExtra("BLOOD_GROUP");

        // Fetch donors from Firebase
        fetchDonors();

        // Handle Request Blood Button Click
        btnRequestBlood.setOnClickListener(v -> requestBlood());
    }

    private void fetchDonors() {
        databaseReference.orderByChild("bloodGroup").equalTo(selectedBloodGroup)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        donorList.clear(); // Clear the list before adding new data
                        for (DataSnapshot donorSnapshot : snapshot.getChildren()) {
                            donor donor = donorSnapshot.getValue(donor.class);
                            if (donor != null) {
                                donorList.add(donor);
                            }
                        }
                        // Notify adapter of data change
                        donorAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DonorListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void requestBlood() {
        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("requests").push();
        String requestId = requestRef.getKey();

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("bloodGroup", selectedBloodGroup);
        requestData.put("timestamp", System.currentTimeMillis());

        requestRef.setValue(requestData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(DonorListActivity.this, "Blood request sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DonorListActivity.this, "Failed to send request.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
