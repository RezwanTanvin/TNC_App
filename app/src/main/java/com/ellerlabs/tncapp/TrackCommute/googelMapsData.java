package com.ellerlabs.tncapp.TrackCommute;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.ellerlabs.tncapp.R;
import com.ellerlabs.tncapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.ellerlabs.tncapp.TrackCommute.databinding.ActivityMapsBinding;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class googelMapsData extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private SQLiteDatabase mSQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSQLiteDatabase = openOrCreateDatabase("location_db", Context.MODE_PRIVATE, null);



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

        Cursor latlong = mSQLiteDatabase.rawQuery("SELECT * FROM  locations ", null);

        List<LatLng> points = new ArrayList<LatLng>();

        if (latlong.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.

                double latitude = latlong.getDouble(0);
                double longitude = latlong.getDouble(1);
                points.add(new LatLng(latitude, longitude));

            } while (latlong.moveToNext());

            PolylineOptions options = new PolylineOptions().width(10).color(Color.RED).geodesic(true);

            for (int i = 0; i < points.size(); i++) {
                LatLng point = points.get(i);
                options.add(point);
            }
            mMap.addPolyline(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 16));

            LatLng Start = points.get(0);
            LatLng Finish = points.get(points.size()-1);

            mMap.addMarker(new MarkerOptions().position(Start).title("Start"));
            //mMap.addMarker(new MarkerOptions().position(Finish).title("End"));
           // mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 3000, null);
        }
    }
}