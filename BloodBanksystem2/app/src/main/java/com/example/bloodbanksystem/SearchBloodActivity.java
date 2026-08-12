package com.example.bloodbanksystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SearchBloodActivity extends AppCompatActivity {
    private Button buttonA1, buttonA2, buttonB1, buttonB2, buttonAB1, buttonAB2, buttonO1, buttonO2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbb); // Ensure correct XML file is set

        // Find buttons by their IDs from XML
        buttonA1 = findViewById(R.id.buttonA1);
        buttonA2 = findViewById(R.id.buttonA2);
        buttonB1 = findViewById(R.id.buttonB1);
        buttonB2 = findViewById(R.id.buttonB2);
        buttonAB1 = findViewById(R.id.buttonAB1);
        buttonAB2 = findViewById(R.id.buttonAB2);
        buttonO1 = findViewById(R.id.buttonO1);
        buttonO2 = findViewById(R.id.buttonO2);

        // Set click listeners for each button
        buttonA1.setOnClickListener(v -> openDonorList("A+"));
        buttonA2.setOnClickListener(v -> openDonorList("A-"));
        buttonB1.setOnClickListener(v -> openDonorList("B+"));
        buttonB2.setOnClickListener(v -> openDonorList("B-"));
        buttonAB1.setOnClickListener(v -> openDonorList("AB+"));
        buttonAB2.setOnClickListener(v -> openDonorList("AB-"));
        buttonO1.setOnClickListener(v -> openDonorList("O+"));
        buttonO2.setOnClickListener(v -> openDonorList("O-"));
    }

    // Open DonorListActivity and pass the selected blood group
    private void openDonorList(String bloodGroup) {
        Intent intent = new Intent(SearchBloodActivity.this, DonorListActivity.class);
        intent.putExtra("BLOOD_GROUP", bloodGroup); // Pass selected blood group
        startActivity(intent); // Start DonorListActivity
    }

}
