package com.ellerlabs.tncapp.ContractorScreen.TrackCommute;


import static com.ellerlabs.tncapp.ContractorScreen.TrackCommute.S4_S_distanceCalculator.distance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ellerlabs.tncapp.R;

import java.text.DecimalFormat;
import java.util.Date;


public class S4_timeAndDistanceTravelled extends AppCompatActivity {

    TextView timer,showDistance, GPSStrength;
    long date;
    boolean timerRunning;
    Intent intent;
    int rowID;
    String mileage, timeElapsed;

    ContentValues values;


    private SQLiteDatabase mSQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_commute_activity_track_time_distance_s4);

        intent = new Intent();
        intent = getIntent();

        if ( intent.getStringExtra("Date")!= null)
        {
            date = intent.getLongExtra("Date",0);
        }
        else{
            date = new Date().getTime();
        }

        rowID = intent.getIntExtra("ID",0);

        if (intent.getStringExtra("Mileage") != null){
            mileage = intent.getStringExtra("Mileage");
        }


        timer = findViewById(R.id.timeTV);
        showDistance = findViewById(R.id.DistanceTV);
        GPSStrength = findViewById(R.id.title4);


        timerRunning = true;

        intent = new Intent(this, S4_S_distanceCalculator.class);
        startService(intent);

    }


    public void onStart(){
        super.onStart();
        showLiveClockAndDistance(timer);

    }

    public void showLiveClockAndDistance(final TextView timer) {

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsedTime = System.currentTimeMillis() - date;
                int seconds = (int) (elapsedTime / 1000) % 60;
                int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
                int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);

                timeElapsed = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timer.setText(timeElapsed);

                Double distance = S4_S_distanceCalculator.getDistance();

                DecimalFormat df = new DecimalFormat("#.##");
                showDistance.setText(df.format(distance) + " KM");


                if (timerRunning) {
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    public void goToS5(View view){


        //TODO: Does not say what to do if the user selects no.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Commute");
        builder.setMessage("Please confirm if you have reached your destination? \n\nThis action cannot be undone.")
                .setCancelable(true)
                .setPositiveButton("Confirm Reached", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                timerRunning= false;
                stopService(intent);

                updateLocationDatabase();
                updateTripCommuteDatabase();


                Intent intent = new Intent (S4_timeAndDistanceTravelled.this, S5_mileageConfirmation.class);

                if (mileage != null ) {

                    double start_mileage = Double.valueOf(mileage);
                    double newMilage = start_mileage + distance;
                    intent.putExtra("New_Mileage", newMilage);
                    intent.putExtra("ID",rowID);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(S4_timeAndDistanceTravelled.this, "Saved successfully. Thank you for logging your commute.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(S4_timeAndDistanceTravelled.this,
                            uploadAllCommuteDataToFirebase.class));
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        builder.create().show();

    }

   // int totalTable;
    public void updateLocationDatabase(){

        mSQLiteDatabase = this.openOrCreateDatabase("location_db", Context.MODE_PRIVATE, null);

       // Need to figure out a way to make sure we are creating TrackCommute ID specific table for locations in case more than one record is created.
        mSQLiteDatabase.execSQL("drop table IF EXISTS  permLocationTable_1");

        mSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS permLocationTable_1  (" +
                "latitude DOUBLE, " +
                "longitude DOUBLE, " +
                "gpsStrength DOUBLE)");


        mSQLiteDatabase.execSQL("INSERT INTO permLocationTable_1 "+
                "SELECT * " +
                "FROM locations");
        }


   public void updateTripCommuteDatabase(){

       mSQLiteDatabase = this.openOrCreateDatabase("TrackCommuteInfo", Context.MODE_PRIVATE, null);

       values = new ContentValues();
       values.put("DRIVE_TIME",  String.valueOf(this.timer.getText()));
       values.put("TOTAL_MILES_DRIVEN_FROM_GPS",String.valueOf(distance));

       //TODO: Make this dynamic where every new record has the table name base on the rowId from Trackcommute.

       values.put("SQL_TABLE_NAME", "permLocationTable_1");

       mSQLiteDatabase.update("TrackCommuteInfo",values,"ID = ?",new String[]{String.valueOf(rowID)});
   }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please avoid pressing the back button, this will corrupt the data collected.", Toast.LENGTH_SHORT).show();
    }



}

