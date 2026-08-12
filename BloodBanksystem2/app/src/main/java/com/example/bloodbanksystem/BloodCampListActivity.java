package com.example.bloodbanksystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
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

public class BloodCampListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CampAdapter adapter;
    private List<BloodCamp> campList;
    private DatabaseReference campRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_camp_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        campList = new ArrayList<>();

        adapter = new CampAdapter(this, campList, camp -> {
            Intent intent = new Intent(BloodCampListActivity.this, CampDetailsActivity.class);
            intent.putExtra("camp_data", camp);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        campRef = FirebaseDatabase.getInstance().getReference("camp_registrations");

        fetchCampData();
    }

    private void fetchCampData() {
        campRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                campList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BloodCamp camp = dataSnapshot.getValue(BloodCamp.class);
                    if (camp != null) {
                        campList.add(camp);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BloodCampListActivity.this, "Failed to load camps", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
