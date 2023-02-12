package com.ellerlabs.tncapp.AdminScreen.CommuteData.ShowMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.ellerlabs.tncapp.R;
import com.ellerlabs.tncapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class googelMapsData extends FragmentActivity implements OnMapReadyCallback {

    Intent intent;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase fdb;
    DatabaseReference fdbRef;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        String key = intent.getStringExtra("Key");
        String startTime = intent.getStringExtra("startTime");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fdb = FirebaseDatabase.getInstance();
        fdbRef = fdb.getReference("Commute Data").child(key).child(startTime).child("Location_Data");



        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        List<LatLng> points = new ArrayList<LatLng>();
        fdbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data: snapshot.getChildren()){

                    double latitude = Double.valueOf(data.child("latitude").getValue().toString());
                    double longitude = Double.valueOf(data.child("longitude").getValue().toString());
                    points.add(new LatLng(latitude, longitude));

                }

                PolylineOptions options = new PolylineOptions().width(10).color(Color.RED).geodesic(true);

                for (int i = 0; i < points.size(); i++) {
                    LatLng point = points.get(i);
                    options.add(point);
                }
                mMap.addPolyline(options);
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 16));

                LatLng Start = points.get(0);
                LatLng Finish = points.get(points.size()-1);

                mMap.addMarker(new MarkerOptions().position(Start).title("Start"));
                mMap.addMarker(new MarkerOptions().position(Finish).title("Finish"));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Start,10));

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(Start);
                builder.include(Finish);
                LatLngBounds bounds = builder.build();
                int padding = 150; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}