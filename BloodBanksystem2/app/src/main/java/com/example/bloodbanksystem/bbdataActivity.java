package com.example.bloodbanksystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class bbdataActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<BloodBank> bloodBankList;
    private BloodBankAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbdata);

        initUI();
        fetchBloodBankData();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bloodBankList = new ArrayList<>();
        adapter = new BloodBankAdapter(this, bloodBankList, this::openDetailsActivity);
        recyclerView.setAdapter(adapter);
    }

    private void fetchBloodBankData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("data");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bloodBankList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BloodBank bloodBank = dataSnapshot.getValue(BloodBank.class);
                    if (bloodBank != null) {
                        bloodBankList.add(bloodBank);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(bbdataActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDetailsActivity(BloodBank bloodBank) {
        // Create a custom AlertDialog for authentication
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Authentication Required");

        // Inflate a custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_authenticate, null);
        builder.setView(dialogView);

        final EditText etBankName = dialogView.findViewById(R.id.et_auth_bank_name);
        final EditText etPassword = dialogView.findViewById(R.id.et_auth_password);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String inputBankName = etBankName.getText().toString().trim();
            String inputPassword = etPassword.getText().toString().trim();

            if (inputBankName.equals(bloodBank.getBloodBankName()) &&
                    inputPassword.equals(bloodBank.getPassword())) {

                Intent intent = new Intent(this, BloodBankDetailsActivity.class);
                intent.putExtra("id", bloodBank.getId());
                intent.putExtra("name", bloodBank.getBloodBankName());
                intent.putExtra("contact", bloodBank.getContactPerson());
                intent.putExtra("phone", bloodBank.getPhoneNumber());
                intent.putExtra("email", bloodBank.getEmail());
                intent.putExtra("address", bloodBank.getAddress());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}