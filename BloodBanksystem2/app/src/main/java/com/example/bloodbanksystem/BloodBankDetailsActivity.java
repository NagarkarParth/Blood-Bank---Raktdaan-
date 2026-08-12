package com.example.bloodbanksystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BloodBankDetailsActivity extends AppCompatActivity {
    private TextView tvName, tvContact, tvPhone, tvEmail, tvAddress;
    private EditText etLatitude, etLongitude;
    private Button btnManageInventory, btnOpenMap, btnSetLocation, btnPickLocation;
    private FloatingActionButton fabAdd; // Declare here

    private DatabaseReference databaseReference;
    private String bloodBankId, bloodBankName;

    // Launcher for MapsActivity to get the selected location
    private final ActivityResultLauncher<Intent> mapLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    double latitude = result.getData().getDoubleExtra("latitude", 0);
                    double longitude = result.getData().getDoubleExtra("longitude", 0);

                    // Set the picked location in EditTexts
                    etLatitude.setText(String.valueOf(latitude));
                    etLongitude.setText(String.valueOf(longitude));
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_details);

        // Initialize Firebase Database (Changed reference to "data")
        databaseReference = FirebaseDatabase.getInstance().getReference("data");

        // Initialize UI Elements
        tvName = findViewById(R.id.tvName);
        tvContact = findViewById(R.id.tvContact);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        btnOpenMap = findViewById(R.id.btnOpenMap);
        btnManageInventory = findViewById(R.id.btnManageInventory);
        btnSetLocation = findViewById(R.id.btnSetLocation);
        btnPickLocation = findViewById(R.id.btnPickLocation);
        fabAdd = findViewById(R.id.fabAdd); // Initialize here

        // Set FAB click listener
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodBankDetailsActivity.this, CampRegistrationActivity.class);
                startActivity(intent);
            }
        });

        // Get Data from Intent
        bloodBankId = getIntent().getStringExtra("id");
        bloodBankName = getIntent().getStringExtra("name");
        String contact = getIntent().getStringExtra("contact");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");
        String address = getIntent().getStringExtra("address");

        // Set values in TextViews
        tvName.setText(bloodBankName != null ? bloodBankName : "N/A");
        tvContact.setText(contact != null ? "Contact: " + contact : "Contact: N/A");
        tvPhone.setText(phone != null && !phone.equals("null") ? "Phone: " + phone : "Phone: Not Available");
        tvEmail.setText(email != null ? "Email: " + email : "Email: N/A");
        tvAddress.setText(address != null ? "Address: " + address : "Address: N/A");

        // Open Google Maps to show saved locations
        btnOpenMap.setOnClickListener(v -> {
            Intent intent = new Intent(BloodBankDetailsActivity.this, MapsActivity.class);
            startActivity(intent);
        });

        // Open MapsActivity to Pick Location
        btnPickLocation.setOnClickListener(v -> {
            Intent intent = new Intent(BloodBankDetailsActivity.this, MapsActivity.class);
            intent.putExtra("pickLocation", true);  // Send flag to MapsActivity
            mapLauncher.launch(intent);
        });

        // Save Location to Firebase
        btnSetLocation.setOnClickListener(v -> {
            String latStr = etLatitude.getText().toString().trim();
            String lonStr = etLongitude.getText().toString().trim();

            if (latStr.isEmpty() || lonStr.isEmpty()) {
                Toast.makeText(this, "Enter Latitude and Longitude", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double latitude = Double.parseDouble(latStr);
                double longitude = Double.parseDouble(lonStr);

                if (bloodBankId == null || bloodBankId.isEmpty()) {
                    Toast.makeText(this, "Blood Bank ID is missing!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save Location under "data" -> "bloodBankId"
                DatabaseReference bloodBankRef = databaseReference.child(bloodBankId);
                bloodBankRef.child("latitude").setValue(latitude);
                bloodBankRef.child("longitude").setValue(longitude);
                bloodBankRef.child("bloodBankName").setValue(bloodBankName);  // Ensure name is stored

                Toast.makeText(this, "Location Saved Successfully!", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid Coordinates", Toast.LENGTH_SHORT).show();
            }
        });

        // Manage Inventory
        btnManageInventory.setOnClickListener(v -> {
            if (bloodBankId == null || bloodBankId.isEmpty()) {
                Toast.makeText(this, "Blood Bank ID is missing!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(BloodBankDetailsActivity.this, ManageInventoryActivity.class);
            intent.putExtra("id", bloodBankId);
            intent.putExtra("name", bloodBankName);
            startActivity(intent);
        });
    }
}
