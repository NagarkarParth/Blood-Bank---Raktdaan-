package com.example.bloodbanksystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class newbbActivity extends AppCompatActivity {

    private List<Button> bloodGroupButtons;
    private Button selectedButton = null;
    private Button searchButton;
    private String selectedBloodGroup = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbb);

        // Initialize the list of buttons
        bloodGroupButtons = new ArrayList<>();
        searchButton = findViewById(R.id.buttonSearch);

        // Add blood group buttons to the list
        bloodGroupButtons.add(findViewById(R.id.buttonA1));
        bloodGroupButtons.add(findViewById(R.id.buttonA2));
        bloodGroupButtons.add(findViewById(R.id.buttonB1));
        bloodGroupButtons.add(findViewById(R.id.buttonB2));
        bloodGroupButtons.add(findViewById(R.id.buttonAB1));
        bloodGroupButtons.add(findViewById(R.id.buttonAB2));
        bloodGroupButtons.add(findViewById(R.id.buttonO1));
        bloodGroupButtons.add(findViewById(R.id.buttonO2));

        // Set click listeners for blood group buttons
        for (Button button : bloodGroupButtons) {
            button.setOnClickListener(v -> selectBloodGroup((Button) v));
        }

        // Click listener for search button
        searchButton.setOnClickListener(v -> {
            if (selectedBloodGroup.isEmpty()) {
                Toast.makeText(newbbActivity.this, "Please select a blood group", Toast.LENGTH_SHORT).show();
            } else {
                // Open donor list activity and pass selected blood group
                Intent intent = new Intent(newbbActivity.this, DonorListActivity.class);
                intent.putExtra("BLOOD_GROUP", selectedBloodGroup);
                startActivity(intent);
            }
        });
    }
    private void selectBloodGroup(Button clickedButton) {
        // Reset all button colors before selecting a new one
        resetButtonColors();

        // Set the new selected button
        selectedButton = clickedButton;
        selectedBloodGroup = clickedButton.getText().toString();
        selectedButton.setBackgroundColor(Color.parseColor("#008000")); // Green for selection

        // Display Toast message
        Toast.makeText(newbbActivity.this, "Selected Blood Group: " + selectedBloodGroup, Toast.LENGTH_SHORT).show();
    }

    // Function to handle blood group selection


    // Reset all button colors
    private void resetButtonColors() {
        for (Button button : bloodGroupButtons) {
            button.setBackgroundColor(Color.parseColor("#D32F2F")); // Default Red
        }
    }
}

