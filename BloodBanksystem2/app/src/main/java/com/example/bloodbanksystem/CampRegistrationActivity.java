package com.example.bloodbanksystem;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CampRegistrationActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etAddress, etCampDate;
    private Button btnRegister, btnSelectImage;
    private ImageView ivCampImage;
    private Uri imageUri;
    private DatabaseReference campRef;
    private StorageReference storageRef;
    private Calendar calendar;
    private ProgressDialog progressDialog;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_registration);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etCampDate = findViewById(R.id.etCampDate);
        btnRegister = findViewById(R.id.btnRegister);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivCampImage = findViewById(R.id.ivCampImage);

        campRef = FirebaseDatabase.getInstance().getReference("camp_registrations");
        storageRef = FirebaseStorage.getInstance().getReference("camp_images");

        calendar = Calendar.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        etCampDate.setOnClickListener(v -> showDatePicker());
        btnSelectImage.setOnClickListener(v -> openFileChooser());
        btnRegister.setOnClickListener(v -> uploadImageAndRegister());
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) ->
                        etCampDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear),
                year, month, day
        );
        datePickerDialog.show();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivCampImage.setImageURI(imageUri);
        }
    }

    private void uploadImageAndRegister() {
        if (!validateInputs()) return;

        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();

        if (imageUri != null) {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference fileRef = storageRef.child(fileName);

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                progressDialog.dismiss();
                                registerCamp(uri.toString());
                            }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(CampRegistrationActivity.this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            registerCamp(""); // Register camp even if no image is uploaded
        }
    }

    private boolean validateInputs() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String campDate = etCampDate.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(campDate)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid Email!");
            return false;
        }

        if (!Patterns.PHONE.matcher(phone).matches() || phone.length() < 10) {
            etPhone.setError("Invalid Phone Number!");
            return false;
        }

        return true;
    }

    private void registerCamp(String imageUrl) {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String campDate = etCampDate.getText().toString().trim();

        progressDialog.setMessage("Registering Camp...");
        progressDialog.show();

        String registrationId = campRef.push().getKey();
        Map<String, Object> campData = new HashMap<>();
        campData.put("name", name);
        campData.put("email", email);
        campData.put("phone", phone);
        campData.put("address", address);
        campData.put("campDate", campDate);
        campData.put("imageUrl", imageUrl);

        campRef.child(registrationId).setValue(campData)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Camp Registered Successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etAddress.setText("");
        etCampDate.setText("");
        ivCampImage.setImageResource(0);
        imageUri = null;
    }
}
