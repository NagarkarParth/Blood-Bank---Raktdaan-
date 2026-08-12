package com.example.bloodbanksystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class donorregisterActivity extends AppCompatActivity {

    private EditText etName, etPhone, etEmail, etPassword;
    private Spinner spinnerBloodGroup;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorregister);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("donors");

        // Initialize SharedPreferences to store login session
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        // Check if user is already logged in
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(donorregisterActivity.this, loginactivity.class));
            finish();
        }

        // Initialize Views
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup); // Spinner for blood groups
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Initialize Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");

        // Populate Spinner with Blood Groups
        String[] bloodGroups = {"Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        spinnerBloodGroup.setAdapter(adapter);

        // Register Button Click Listener
        btnRegister.setOnClickListener(v -> registerDonor());
    }
    private void registerDonor() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter Full Name");
            return;
        }
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            etPhone.setError("Enter Valid Phone Number");
            return;
        }
        if ("Select Blood Group".equals(bloodGroup)) {
            Toast.makeText(this, "Please select a blood group", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter Email Address");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        progressDialog.show();

        // Create User in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            // Fetch FCM Token
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(task1 -> {
                                        if (!task1.isSuccessful()) {
                                            Log.w("FCM", "Fetching FCM token failed", task1.getException());
                                            return;
                                        }

                                        // Get new FCM registration token
                                        String fcmToken = task1.getResult();

                                        // Save donor details including FCM Token
                                        donor donor = new donor(name, phone, bloodGroup, email, fcmToken);

                                        databaseReference.child(userId).setValue(donor)
                                                .addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        saveLoginSession(userId);
                                                        Toast.makeText(donorregisterActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(donorregisterActivity.this, donorActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(donorregisterActivity.this, "Database Error: " + task2.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    });
                        }
                    } else {
                        Toast.makeText(donorregisterActivity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Save login session in SharedPreferences
    private void saveLoginSession(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_ID", userId);
        editor.putBoolean("IS_LOGGED_IN", true);
        editor.apply();
    }
}
