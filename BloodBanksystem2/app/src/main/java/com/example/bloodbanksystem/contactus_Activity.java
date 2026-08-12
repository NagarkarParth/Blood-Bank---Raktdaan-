package com.example.bloodbanksystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class contactus_Activity extends AppCompatActivity {

    private TextView about;
    private TextView home;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contactus);

        img = findViewById(R.id.btn_back);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contactus_Activity.this,UserActivity.class);
                startActivity(intent);
            }
        });
        home = findViewById(R.id.tv_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contactus_Activity.this,loginactivity.class);
                startActivity(intent);
            }
        });
        about = findViewById(R.id.tv_about_us);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contactus_Activity.this,aboutus_Activity.class);
                startActivity(intent);
            }
        });
    }
}