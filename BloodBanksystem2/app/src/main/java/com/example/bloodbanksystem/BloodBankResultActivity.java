package com.example.bloodbanksystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BloodBankResultActivity extends AppCompatActivity {

    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_result);

        resultText = findViewById(R.id.txt_result1);

        String resultData = getIntent().getStringExtra("resultData");
        resultText.setText(resultData != null ? resultData : "No data received");
    }
}