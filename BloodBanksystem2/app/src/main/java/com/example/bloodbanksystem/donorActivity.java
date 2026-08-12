package com.example.bloodbanksystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class donorActivity extends AppCompatActivity {

    private Button btnRegisterDonor, btnSearchBlood, btnLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor2);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Buttons
        btnRegisterDonor = findViewById(R.id.btnRegisterDonor);
        btnSearchBlood = findViewById(R.id.btnSearchBlood);
        btnLogout = findViewById(R.id.btnLogout); // Make sure this ID exists in your XML

        // Navigate to Register Donor Activity
        btnRegisterDonor.setOnClickListener(v -> {
            Intent intent = new Intent(donorActivity.this, donorregisterActivity.class);
            startActivity(intent);
        });

        // Navigate to Search Blood Activity
        btnSearchBlood.setOnClickListener(v -> {
            Intent intent = new Intent(donorActivity.this, newbbActivity.class);
            startActivity(intent);
        });

        // Logout Button Functionality
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut(); // Logout from Firebase
            Toast.makeText(donorActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

            // Redirect to LoginActivity
            Intent intent = new Intent(donorActivity.this, loginactivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}
