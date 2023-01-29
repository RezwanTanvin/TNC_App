package com.ellerlabs.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ellerlabs.tncapp.MainActivity;
import com.ellerlabs.tncapp.R;

import java.util.Date;

public class trackCommS1 extends AppCompatActivity {

    SQLiteDatabase db;
    Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_comm_s1);

        db = openOrCreateDatabase("TrackCommuteInfo",MODE_PRIVATE,null);

        db.execSQL("CREATE TABLE IF NOT EXISTS TrackCommuteInfo (" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ," +
                "VEHICLE_OPERATOR varchar(32)," +
                "VEHICLE_TYPE varchar(32)," +
                "TRIP_TYPE varchar(32)," +
                "STARTING_MILEAGE varchar(32)," +
                "STARTED_DRIVING_AT varchar(32)," +
                "OVERRIDE_MILEAGE varchar(32)," +
                "FINAL_MILEAGE varchar(32))");


    }




    public void startTrackCommuteS2(View view){

       // date = new Date();
        Intent intent = new Intent(this, trackCommS2.class);
        //intent.putExtra("DATETIME_FLOW_STARTED",date.toString());
        startActivity(intent);
    }

    public void goToMainScreen(View view){
        Toast.makeText(this, "You are not required to track your vehicle commute.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
    }

}