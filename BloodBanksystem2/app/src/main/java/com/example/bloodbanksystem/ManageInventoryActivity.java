package com.example.bloodbanksystem;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManageInventoryActivity extends AppCompatActivity {

    private EditText etQuantity, etDateOfCollection;
    private Button btnAddInventory;
    private Spinner spinnerBloodGroup;
    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private List<InventoryModel> inventoryList;
    private DatabaseReference databaseReference;
    private String bloodBankId;
    private String selectedBloodGroup;
    private String dateOfExpiration;  // Store calculated expiration date
    private Button btnRemoveInventory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);

        // Initialize UI elements
        etQuantity = findViewById(R.id.etQuantity);
        etDateOfCollection = findViewById(R.id.etDateOfCollection);
        btnAddInventory = findViewById(R.id.btnAddInventory);
        recyclerView = findViewById(R.id.recyclerViewInventory);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        // Declare button

        // Inside onCreate()
        btnRemoveInventory = findViewById(R.id.btnRemoveInventory);
        btnRemoveInventory.setOnClickListener(v -> removeInventory());

        // Get Blood Bank ID
        bloodBankId = getIntent().getStringExtra("id");
        if (bloodBankId == null) {
            Toast.makeText(this, "Blood Bank ID not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("data")
                .child(bloodBankId)
                .child("inventory");

        // Setup RecyclerView
        inventoryList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        inventoryAdapter = new InventoryAdapter(inventoryList);
        recyclerView.setAdapter(inventoryAdapter);

        // Populate Spinner (Dropdown) with Blood Groups
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.blood_groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodGroup.setAdapter(adapter);

        // Set default selection and listener
        spinnerBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBloodGroup = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedBloodGroup = null;
            }
        });

        // Date picker for collection date
        etDateOfCollection.setOnClickListener(v -> showDatePickerDialog());

        // Fetch existing inventory
        fetchInventory();

        // Add new inventory
        btnAddInventory.setOnClickListener(v -> addInventory());
    }

    private void addInventory() {
        if (selectedBloodGroup == null || selectedBloodGroup.isEmpty()) {
            Toast.makeText(this, "Please select a blood group", Toast.LENGTH_SHORT).show();
            return;
        }

        String quantityStr = etQuantity.getText().toString().trim();
        String dateOfCollection = etDateOfCollection.getText().toString().trim();

        if (TextUtils.isEmpty(quantityStr) || TextUtils.isEmpty(dateOfCollection)) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);

        // Query to check if the blood group already exists
        databaseReference.orderByChild("bloodGroup").equalTo(selectedBloodGroup)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                InventoryModel existingInventory = dataSnapshot.getValue(InventoryModel.class);
                                if (existingInventory != null) {
                                    int newQuantity = existingInventory.getQuantity() + quantity;
                                    String inventoryId = existingInventory.getId();

                                    databaseReference.child(inventoryId).child("quantity").setValue(newQuantity);
                                    databaseReference.child(inventoryId).child("dateOfCollection").setValue(dateOfCollection);
                                    databaseReference.child(inventoryId).child("dateOfExpiration").setValue(dateOfExpiration)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(ManageInventoryActivity.this, "Inventory updated successfully!", Toast.LENGTH_SHORT).show();
                                                etQuantity.setText("");
                                                etDateOfCollection.setText("");
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(ManageInventoryActivity.this, "Failed to update inventory", Toast.LENGTH_SHORT).show());
                                }
                            }
                        } else {
                            String inventoryId = databaseReference.push().getKey();
                            InventoryModel inventory = new InventoryModel(inventoryId, selectedBloodGroup, quantity, dateOfCollection, dateOfExpiration);

                            databaseReference.child(inventoryId).setValue(inventory)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ManageInventoryActivity.this, "Inventory added successfully!", Toast.LENGTH_SHORT).show();
                                        etQuantity.setText("");
                                        etDateOfCollection.setText("");
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ManageInventoryActivity.this, "Failed to add inventory", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ManageInventoryActivity.this, "Failed to check inventory", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void removeInventory() {
        if (selectedBloodGroup == null || selectedBloodGroup.isEmpty()) {
            Toast.makeText(this, "Please select a blood group", Toast.LENGTH_SHORT).show();
            return;
        }

        String quantityStr = etQuantity.getText().toString().trim();
        if (TextUtils.isEmpty(quantityStr)) {
            Toast.makeText(this, "Please enter a quantity to remove", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantityToRemove = Integer.parseInt(quantityStr);

        // Fetch the existing inventory for the selected blood group
        databaseReference.orderByChild("bloodGroup").equalTo(selectedBloodGroup)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                InventoryModel existingInventory = dataSnapshot.getValue(InventoryModel.class);
                                if (existingInventory != null) {
                                    int currentQuantity = existingInventory.getQuantity();
                                    String inventoryId = existingInventory.getId();

                                    // Ensure quantity doesn't go negative
                                    int newQuantity = Math.max(0, currentQuantity - quantityToRemove);

                                    if (newQuantity == 0) {
                                        // Remove entry if quantity reaches zero
                                        databaseReference.child(inventoryId).removeValue()
                                                .addOnSuccessListener(aVoid -> Toast.makeText(ManageInventoryActivity.this, "Inventory item removed!", Toast.LENGTH_SHORT).show())
                                                .addOnFailureListener(e -> Toast.makeText(ManageInventoryActivity.this, "Failed to remove inventory", Toast.LENGTH_SHORT).show());
                                    } else {
                                        // Update quantity in Firebase
                                        databaseReference.child(inventoryId).child("quantity").setValue(newQuantity)
                                                .addOnSuccessListener(aVoid -> Toast.makeText(ManageInventoryActivity.this, "Quantity updated successfully!", Toast.LENGTH_SHORT).show())
                                                .addOnFailureListener(e -> Toast.makeText(ManageInventoryActivity.this, "Failed to update inventory", Toast.LENGTH_SHORT).show());
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(ManageInventoryActivity.this, "No inventory found for selected blood group", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ManageInventoryActivity.this, "Failed to update inventory", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchInventory() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                inventoryList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    InventoryModel inventory = dataSnapshot.getValue(InventoryModel.class);
                    inventoryList.add(inventory);
                }
                inventoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageInventoryActivity.this, "Failed to load inventory", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            calendar.set(year1, month1, dayOfMonth);
            String dateOfCollection = sdf.format(calendar.getTime());
            etDateOfCollection.setText(dateOfCollection);

            // ✅ Automatically Calculate Expiration Date (35 Days After Collection)
            calendar.add(Calendar.DAY_OF_MONTH, 35);
            dateOfExpiration = sdf.format(calendar.getTime()); // Store expiration date
        }, year, month, day);

        datePickerDialog.show();
    }
}
