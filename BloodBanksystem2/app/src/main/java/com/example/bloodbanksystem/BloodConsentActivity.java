package com.example.bloodbanksystem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class BloodConsentActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etBloodGroup;
    private RadioGroup rgDonatedPreviously, rgDiscomfortAfterDonation;
    private CheckBox cbWeightLoss, cbDiarrhea, cbSwollenGlands, cbLowFever;
    private CheckBox cbHeartDisease, cbCancer, cbDiabetes, cbHepatitis, cbEpilepsy;
    private CheckBox cbDonorConsent;
    private Button btnSubmit;

    private DatabaseReference donorsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_consent);

        donorsRef = FirebaseDatabase.getInstance().getReference("donors"); // Firebase reference

        etFullName = findViewById(R.id.et_full_name);
        etPhone = findViewById(R.id.et_phone_number);
        etBloodGroup = findViewById(R.id.et_blood_group);
        rgDonatedPreviously = findViewById(R.id.rg_donated_previously);
        rgDiscomfortAfterDonation = findViewById(R.id.rg_discomfort_after_donation);
        cbWeightLoss = findViewById(R.id.cb_weight_loss);
        cbDiarrhea = findViewById(R.id.cb_diarrhea);
        cbSwollenGlands = findViewById(R.id.cb_swollen_glands);
        cbLowFever = findViewById(R.id.cb_low_fever);
        cbHeartDisease = findViewById(R.id.cb_heart_disease);
        cbCancer = findViewById(R.id.cb_cancer);
        cbDiabetes = findViewById(R.id.cb_diabetes);
        cbHepatitis = findViewById(R.id.cb_hepatitis);
        cbEpilepsy = findViewById(R.id.cb_epilepsy);
        cbDonorConsent = findViewById(R.id.cb_donor_consent); // New Consent Checkbox
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(v -> submitForm());

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 10) { // Ensure at least 10 digits are entered
                    fetchDonorData(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchDonorData(String phoneNumber) {
        String cleanedPhone = phoneNumber.replaceAll("[^0-9]", "");
        Log.d("FetchData", "Searching for phone number: " + cleanedPhone);

        donorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean donorFound = false;

                for (DataSnapshot donorSnapshot : snapshot.getChildren()) {
                    String storedPhone = donorSnapshot.child("phone").getValue(String.class);
                    if (storedPhone != null && storedPhone.equals(cleanedPhone)) {
                        donorFound = true;
                        etFullName.setText(donorSnapshot.child("fullName").getValue(String.class));
                        etBloodGroup.setText(donorSnapshot.child("bloodGroup").getValue(String.class));

                        Toast.makeText(BloodConsentActivity.this, "Donor details loaded!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (!donorFound) {
                    etFullName.setText("");
                    etBloodGroup.setText("");
                    Toast.makeText(BloodConsentActivity.this, "Donor not found! Please register first.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BloodConsentActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitForm() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim().replaceAll("[^0-9]", "");
        String bloodGroup = etBloodGroup.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbDonorConsent.isChecked()) {
            Toast.makeText(this, "You must agree to donate blood at your own responsibility", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("PhoneCheck", "Searching for phone number: " + phone);

        donorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean donorFound = false;
                String donorKey = null;

                for (DataSnapshot donorSnapshot : snapshot.getChildren()) {
                    String storedPhone = donorSnapshot.child("phone").getValue(String.class);

                    if (storedPhone != null && storedPhone.equals(phone)) {
                        donorFound = true;
                        donorKey = donorSnapshot.getKey();

                        if (donorSnapshot.hasChild("consentForm")) {
                            Toast.makeText(BloodConsentActivity.this, "You have already submitted the consent form.", Toast.LENGTH_LONG).show();
                            btnSubmit.setEnabled(false);
                            return;
                        }
                        break;
                    }
                }

                if (donorFound) {
                    updateExistingDonor(donorKey);
                } else {
                    Toast.makeText(BloodConsentActivity.this, "Donor not found! Please register first.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BloodConsentActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateExistingDonor(String donorKey) {
        Map<String, Object> updates = new HashMap<>();

        updates.put("fullName", etFullName.getText().toString().trim());
        updates.put("bloodGroup", etBloodGroup.getText().toString().trim());

        Map<String, Object> consentFormData = new HashMap<>();
        consentFormData.put("donatedBefore", getSelectedRadioText(rgDonatedPreviously));
        consentFormData.put("discomfortAfterDonation", getSelectedRadioText(rgDiscomfortAfterDonation));
        consentFormData.put("unexpectedWeightLoss", cbWeightLoss.isChecked());
        consentFormData.put("repeatedDiarrhea", cbDiarrhea.isChecked());
        consentFormData.put("swollenGlands", cbSwollenGlands.isChecked());
        consentFormData.put("continuousLowFever", cbLowFever.isChecked());
        consentFormData.put("heartDisease", cbHeartDisease.isChecked());
        consentFormData.put("cancer", cbCancer.isChecked());
        consentFormData.put("diabetes", cbDiabetes.isChecked());
        consentFormData.put("hepatitis", cbHepatitis.isChecked());
        consentFormData.put("epilepsy", cbEpilepsy.isChecked());
        consentFormData.put("donorConsent", true);

        updates.put("consentForm", consentFormData);

        donorsRef.child(donorKey).updateChildren(updates)
                .addOnSuccessListener(aVoid -> Toast.makeText(BloodConsentActivity.this, "Consent form submitted successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(BloodConsentActivity.this, "Error updating donor data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    private String getSelectedRadioText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
        return "Not Selected";
    }
}
