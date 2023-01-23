package com.example.tncapp;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private FirebaseAuth mAuth ;
    private FirebaseUser user;

    TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.Hi);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[]{
                    ACCESS_FINE_LOCATION,
                    ACCESS_BACKGROUND_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    ACCESS_WIFI_STATE,
                    CAMERA,
                    AUDIO_SERVICE,
                    WRITE_EXTERNAL_STORAGE,
                    READ_EXTERNAL_STORAGE,
            }, PERMISSION_REQUEST_CODE);
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        String UID = user.getUid();
        userName.setText("Hello\n" + user.getDisplayName());


    }

    public void startTrackCommute(View view){
        startActivity(new Intent(this,com.example.tncapp.TrackCommute.trackCommS1.class));
    }

}