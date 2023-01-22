package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tncapp.MainActivity;
import com.example.tncapp.R;
import com.google.firebase.FirebaseApp;

public class trackCommS1 extends AppCompatActivity {

    SQLiteDatabase TrackCommuteInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_comm_s1);

        TrackCommuteInfo = openOrCreateDatabase("TrackCommuteInfo",MODE_PRIVATE,null);

    }

    public void createEntryforCommutor(){

    }

    public void startTrackCommuteS2(View view){
        startActivity(new Intent(this,com.example.tncapp.TrackCommute.trackCommS2.class));
    }

    public void goToMainScreen(View view){
        Toast.makeText(this, "You are not required to track your vehicle commute.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
    }

}