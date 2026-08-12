package com.example.bloodbanksystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        // Check user authentication status
        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        new Handler().postDelayed(() -> {
            if (currentUser != null && isLoggedIn) {
                // Redirect to Dashboard if user is logged in
                startActivity(new Intent(MainActivity.this, loginactivity.class));
            } else {
                // Redirect to LoginActivity if user is not logged in
                startActivity(new Intent(MainActivity.this, loginactivity.class));
            }
            finish(); // Close MainActivity after navigation
        }, 1500); // Delay for 1.5 seconds
    }
}
