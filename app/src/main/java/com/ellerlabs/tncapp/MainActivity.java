package com.ellerlabs.tncapp;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ellerlabs.tncapp.TrackCommute.S1_areYouDriver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private FirebaseAuth mAuth ;
    private FirebaseUser user;

    TextView userName;

    Button startBtn, inProgressBtn;

    ImageView imageView1,imageView2,imageView3;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.Hi);
        startBtn = findViewById(R.id.startTaskBtn);
        inProgressBtn = findViewById(R.id.inProgressBtn);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[]{
                    ACCESS_FINE_LOCATION,
                    ACCESS_BACKGROUND_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    ACCESS_WIFI_STATE,
                    CAMERA,
                    NOTIFICATION_SERVICE,
                    POST_NOTIFICATIONS,
                    AUDIO_SERVICE,
                    WRITE_EXTERNAL_STORAGE,
                    READ_EXTERNAL_STORAGE,
            }, PERMISSION_REQUEST_CODE);
        };
        //TODO : Will not work for multiple users on a device when loggin in offline. Keep showing the same username (last loggedin user).This only happens when offline.

        userName.setText("Hello\n" + user.getDisplayName());


        //TODO : For some weird reason, I am unable to change the color of a button. Not sure why.
//        startBtn.setBackgroundColor(Color.WHITE);
//        startBtn.setTextColor(Color.BLACK);
//        inProgressBtn.setBackgroundColor(Color.BLACK);
//        inProgressBtn.setTextColor(Color.WHITE);

        imageView1 = findViewById(R.id.trackCommuteImage);
        imageView2 = findViewById(R.id.harvestProductImage);
        imageView3 = findViewById(R.id.processHarvestImage);

        imageView1.setClipToOutline(true);
        imageView2.setClipToOutline(true);
        imageView3.setClipToOutline(true);


        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.track_commmute_logo);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.harvest_product_image);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.process_harvest_logo);

        imageView1.setImageBitmap(bitmap1);
        imageView2.setImageBitmap(bitmap2);
        imageView3.setImageBitmap(bitmap3);



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void startTrackCommute(View view){
        startActivity(new Intent(this, S1_areYouDriver.class));
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please avoid pressing the back button, this will corrupt the data collected.", Toast.LENGTH_LONG).show();
    }

}