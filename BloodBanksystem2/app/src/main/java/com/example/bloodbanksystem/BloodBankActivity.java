package com.example.bloodbanksystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.text.TextUtils;

import java.util.HashMap;

public class BloodBankActivity extends AppCompatActivity {

    private EditText etBloodBankName, etContactPerson, etPhone, etEmail, etAddress, etPassword;
    private Button btnRegister;
    private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        // Initialize UI Components
        etBloodBankName = findViewById(R.id.et_blood_bank_name);
        etContactPerson = findViewById(R.id.et_contact_person);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etAddress = findViewById(R.id.et_address);
        btnRegister = findViewById(R.id.btn_register);
        tvSkip = findViewById(R.id.skiptext);

        // Skip button click
        tvSkip.setOnClickListener(v -> startActivity(new Intent(BloodBankActivity.this, bbdataActivity.class)));

        // Register button click
        btnRegister.setOnClickListener(v -> {
            String bloodBankName = etBloodBankName.getText().toString().trim();
            String contactPerson = etContactPerson.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            // Validate Input
            if (!validateInput(bloodBankName, etBloodBankName, "Enter the Blood Bank Name") ||
                    !validateInput(contactPerson, etContactPerson, "Enter the Contact Person Name") ||
                    !validateInput(phone, etPhone, "Enter the Phone Number") ||
                    !validateInput(email, etEmail, "Enter the Email ID") ||
                    !validateInput(password, etPassword, "Enter the Password") ||
                    !validateInput(address, etAddress, "Enter the Address")) {
                return;
            }

            // Send data to Firebase
            sendDataToFirebase(bloodBankName, contactPerson, phone, email, password, address);

            // Navigate to Blood Bank Data Activity
            startActivity(new Intent(BloodBankActivity.this, bbdataActivity.class));
        });
    }

    private boolean validateInput(String input, EditText editText, String errorMessage) {
        if (TextUtils.isEmpty(input)) {
            editText.setError(errorMessage);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private void sendDataToFirebase(String bloodBankName, String contactPerson, String phone, String email, String password, String address) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("data");

        String id = databaseRef.push().getKey();
        if (id == null) {
            Toast.makeText(this, "Error generating ID", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> bloodBankData = new HashMap<>();
        bloodBankData.put("id", id);
        bloodBankData.put("bloodBankName", bloodBankName);
        bloodBankData.put("contactPerson", contactPerson);
        bloodBankData.put("phone", phone);
        bloodBankData.put("email", email);
        bloodBankData.put("password", password); // storing password
        bloodBankData.put("address", address);

        databaseRef.child(id).setValue(bloodBankData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(BloodBankActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(BloodBankActivity.this, "Failed to Add Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        etBloodBankName.getText().clear();
        etContactPerson.getText().clear();
        etPhone.getText().clear();
        etEmail.getText().clear();
        etPassword.getText().clear();
        etAddress.getText().clear();
    }
}