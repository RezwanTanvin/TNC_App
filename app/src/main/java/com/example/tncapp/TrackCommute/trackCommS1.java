package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tncapp.MainActivity;
import com.example.tncapp.R;

public class trackCommS1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_comm_s1);


    }

    public void startTrackCommuteS2(View view){
        startActivity(new Intent(this,com.example.tncapp.TrackCommute.trackCommS2.class));
    }

    public void goToMainScreen(View view){
        Toast.makeText(this, "You are not required to track your commute in a vehicle if you are not the operator.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
    }

}