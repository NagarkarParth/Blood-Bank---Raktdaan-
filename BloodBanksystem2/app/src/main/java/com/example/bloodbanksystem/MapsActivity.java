package com.example.bloodbanksystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private boolean isPickingLocation = false;
    private Marker selectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Check if user is selecting a location
        isPickingLocation = getIntent().getBooleanExtra("pickLocation", false);
        databaseReference = FirebaseDatabase.getInstance().getReference("data");  // Updated to "data"

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set Default Location to India
        LatLng indiaLocation = new LatLng(20.5937, 78.9629);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indiaLocation, 5));

        // Fetch and display saved blood bank locations
        fetchSavedLocations();

        // Allow user to pick a location if in selection mode
        if (isPickingLocation) {
            mMap.setOnMapClickListener(latLng -> {
                if (selectedMarker != null) {
                    selectedMarker.remove();
                }
                selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

                // Return selected location to BloodBankDetailsActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", latLng.latitude);
                resultIntent.putExtra("longitude", latLng.longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }
    }


    private void fetchSavedLocations() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean hasLocations = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Double latitude = snapshot.child("latitude").getValue(Double.class);
                    Double longitude = snapshot.child("longitude").getValue(Double.class);
                    String bloodBankName = snapshot.child("bloodBankName").getValue(String.class);  // Updated key name

                    if (latitude != null && longitude != null) {
                        LatLng location = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(location).title(bloodBankName));
                        hasLocations = true;
                    }
                }
                // Set default camera position if no locations exist
                if (!hasLocations) {
                    LatLng defaultLocation = new LatLng(20.5937, 78.9629); // Default: India
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 5));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Error loading locations", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
