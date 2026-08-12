package com.example.bloodbanksystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchbloodbankActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    Button aPositive, aNegative, bPositive, bNegative,
            abPositive, abNegative, oPositive, oNegative, searchBtn;

    String selectedGroup = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbloodbank);

        databaseReference = FirebaseDatabase.getInstance().getReference("data");

        // Initialize buttons
        aPositive = findViewById(R.id.btn_a_positive);
        aNegative = findViewById(R.id.btn_a_negative);
        bPositive = findViewById(R.id.btn_b_positive);
        bNegative = findViewById(R.id.btn_b_negative);
        abPositive = findViewById(R.id.btn_ab_positive);
        abNegative = findViewById(R.id.btn_ab_negative);
        oPositive = findViewById(R.id.btn_o_positive);
        oNegative = findViewById(R.id.btn_o_negative);
        searchBtn = findViewById(R.id.btn_search);

        // Set click listeners for blood group selection
        setGroupListener(aPositive, "A+");
        setGroupListener(aNegative, "A-");
        setGroupListener(bPositive, "B+");
        setGroupListener(bNegative, "B-");
        setGroupListener(abPositive, "AB+");
        setGroupListener(abNegative, "AB-");
        setGroupListener(oPositive, "O+");
        setGroupListener(oNegative, "O-");

        // Search button click listener
        searchBtn.setOnClickListener(view -> {
            Log.d("SearchButton", "Search button clicked"); // Debugging log

            if (selectedGroup.isEmpty()) {
                Toast.makeText(this, "Please select a blood group", Toast.LENGTH_SHORT).show();
                Log.d("SearchButton", "No blood group selected"); // Debugging log
            } else {
                Log.d("SearchButton", "Selected group: " + selectedGroup);
                searchBloodBanks(selectedGroup);
            }
        });
    }

    private void setGroupListener(Button button, String group) {
        button.setOnClickListener(v -> {
            selectedGroup = group;
            Toast.makeText(this, "Selected: " + group, Toast.LENGTH_SHORT).show();
            Log.d("BloodGroupSelection", "User selected: " + group);
        });
    }

    private void searchBloodBanks(String selectedBloodGroup) {
        Log.d("FirebaseSearch", "Searching for blood group: " + selectedBloodGroup);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseSearch", "Data received from Firebase");

                StringBuilder resultBuilder = new StringBuilder();

                for (DataSnapshot bloodBankSnapshot : snapshot.getChildren()) {
                    String bloodBankName = bloodBankSnapshot.child("bloodBankName").getValue(String.class);
                    String address = bloodBankSnapshot.child("address").getValue(String.class);
                    String contact = bloodBankSnapshot.child("contactPerson").getValue(String.class);
                    String email = bloodBankSnapshot.child("email").getValue(String.class);
                    String phone = bloodBankSnapshot.child("phoneNumber").getValue(String.class);

                    DataSnapshot inventorySnapshot = bloodBankSnapshot.child("inventory");
                    boolean hasGroup = false;
                    StringBuilder groupDetails = new StringBuilder();

                    for (DataSnapshot item : inventorySnapshot.getChildren()) {
                        String group = item.child("bloodGroup").getValue(String.class);

                        Object quantityObj = item.child("quantity").getValue();
                        String quantity = quantityObj != null ? String.valueOf(quantityObj) : "0";

                        if (group != null && group.equalsIgnoreCase(selectedBloodGroup)
                                && !quantity.equals("0")) {

                            String dateCollected = item.child("dateOfCollection").getValue(String.class);
                            String dateExpire = item.child("dateOfExpiration").getValue(String.class);

                            hasGroup = true;
                            groupDetails.append("Blood Group: ").append(group).append("\n")
                                    .append("Quantity: ").append(quantity).append("\n")
                                    .append("Collected: ").append(dateCollected != null ? dateCollected : "N/A").append("\n")
                                    .append("Expires: ").append(dateExpire != null ? dateExpire : "N/A").append("\n\n");
                        }
                    }

                    if (hasGroup) {
                        resultBuilder.append("Blood Bank: ").append(bloodBankName != null ? bloodBankName : "Unknown").append("\n")
                                .append("Address: ").append(address != null ? address : "N/A").append("\n")
                                .append("Contact: ").append(contact != null ? contact : "N/A").append("\n")
                                .append("Phone: ").append(phone != null ? phone : "N/A").append("\n")
                                .append("Email: ").append(email != null ? email : "N/A").append("\n")
                                .append(groupDetails)
                                .append("--------\n\n");
                    }
                }

                String finalResult = resultBuilder.toString();
                if (!finalResult.isEmpty()) {
                    Log.d("FirebaseSearch", "Results found, opening results activity");

                    Intent intent = new Intent(SearchbloodbankActivity.this, BloodBankResultActivity.class);
                    intent.putExtra("resultData", finalResult);
                    startActivity(intent);
                } else {
                    Log.d("FirebaseSearch", "No matching blood banks found");
                    Toast.makeText(SearchbloodbankActivity.this, "No matching blood banks found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseSearch", "Error fetching data: " + error.getMessage());
                Toast.makeText(SearchbloodbankActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
