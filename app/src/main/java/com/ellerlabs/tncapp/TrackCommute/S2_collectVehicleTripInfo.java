package com.ellerlabs.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ellerlabs.tncapp.ImagePreview;
import com.ellerlabs.tncapp.R;

import java.util.Date;

public class S2_collectVehicleTripInfo extends AppCompatActivity {
    Button atv;
    Button snowmobile;
    Button truck ;
    Button dayTrip;
    Button overNightTrip;

    Button driveNow;
    Date startedDrivingAt;

    Boolean flag1;
    Boolean flag2;

    SQLiteDatabase db;

    ContentValues values;

    Intent intent ;

    String mileage;
    ImageView cameraIcon;
    String FilePath;
    Boolean hasFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_vehicle_trip_info_s2);

        atv = findViewById(R.id.atvBtn);
        atv.setBackgroundColor(Color.DKGRAY);
        atv.setTextColor(Color.GRAY);

        snowmobile = findViewById(R.id.snwoMobileBtn);
        snowmobile.setBackgroundColor(Color.DKGRAY);
        snowmobile.setTextColor(Color.GRAY);

        truck = findViewById(R.id.truckBtn);
        truck.setBackgroundColor(Color.DKGRAY);
        truck.setTextColor(Color.GRAY);

        dayTrip = findViewById(R.id.dayTripBtn);
        dayTrip.setBackgroundColor(Color.DKGRAY);
        dayTrip.setTextColor(Color.GRAY);

        overNightTrip = findViewById(R.id.overNightBtn);
        overNightTrip.setBackgroundColor(Color.DKGRAY);
        overNightTrip.setTextColor(Color.GRAY);

        driveNow = findViewById(R.id.startDrivingBtn);
        driveNow.setEnabled(false);
        cameraIcon = findViewById(R.id.cameraIcon);

        flag1 = false;
        flag2 = false;

        values = new ContentValues();

        intent = getIntent();

//        if(intent.getStringExtra("DATETIME_FLOW_STARTED") != null)
//        {
//            date = intent.getStringExtra("DATETIME_FLOW_STARTED");
//        }

        if ( intent.getStringExtra("Mileage")!= null)
        {
            mileage = intent.getStringExtra("Mileage");

            truck.setBackgroundColor(Color.WHITE);
            truck.setTextColor(Color.BLACK);

            values.put("VEHICLE_TYPE","Truck");
            values.put("STARTING_MILEAGE",mileage);

            flag1 = true;
            cameraIcon.setVisibility(View.VISIBLE);

        };
        if ( intent.getStringExtra("FilePath")!= null)
        {
            FilePath = intent.getStringExtra("FilePath");
            hasFilePath = true;
        }
        
        db = openOrCreateDatabase("TrackCommuteInfo",MODE_PRIVATE,null);


    }

    public void onStart(){
        super.onStart();

        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(S2_collectVehicleTripInfo.this, ImagePreview.class);
                intent.putExtra("FilePath", FilePath);
                startActivity(intent);
            }
        });

        atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atv.setBackgroundColor(Color.WHITE);
                atv.setTextColor(Color.BLACK);
                snowmobile.setBackgroundColor(Color.DKGRAY);
                snowmobile.setTextColor(Color.GRAY);
                truck.setBackgroundColor(Color.DKGRAY);
                truck.setTextColor(Color.GRAY);
                values.put("VEHICLE_TYPE","ATV");
                flag1 = true;
                enableDriveButton();
            }
        });

        snowmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snowmobile.setBackgroundColor(Color.WHITE);
                snowmobile.setTextColor(Color.BLACK);
                atv.setBackgroundColor(Color.DKGRAY);
                atv.setTextColor(Color.GRAY);
                truck.setBackgroundColor(Color.DKGRAY);
                truck.setTextColor(Color.GRAY);
                values.put("VEHICLE_TYPE","Snowmobile");
                flag1 = true;
                enableDriveButton();
            }
        });

        dayTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayTrip.setBackgroundColor(Color.WHITE);
                dayTrip.setTextColor(Color.BLACK);
                overNightTrip.setBackgroundColor(Color.DKGRAY);
                overNightTrip.setTextColor(Color.GRAY);
                values.put("TRIP_TYPE","Day Trip");
                flag2 = true;
                enableDriveButton();
            }
        });

        overNightTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overNightTrip.setBackgroundColor(Color.WHITE);
                overNightTrip.setTextColor(Color.BLACK);
                dayTrip.setBackgroundColor(Color.DKGRAY);
                dayTrip.setTextColor(Color.GRAY);
                values.put("TRIP_TYPE","Overnight Trip");
                flag2 = true;
                enableDriveButton();
            }

        });

    }



    public void gotToS3(View view) // Truck button onClick calls this
    {
        truck.setBackgroundColor(Color.WHITE);
        truck.setTextColor(Color.BLACK);
        atv.setBackgroundColor(Color.DKGRAY);
        atv.setTextColor(Color.GRAY);
        truck.setBackgroundColor(Color.DKGRAY);
        truck.setTextColor(Color.GRAY);
        values.put("VEHICLE_TYPE","Truck");
        flag1 = true;
        enableDriveButton();

        Intent intent = new Intent(S2_collectVehicleTripInfo.this, S3_captureOdometer.class);

        if(FilePath!= null && mileage != null){
            intent.putExtra("FilePath",FilePath);
            intent.putExtra("Mileage",mileage);
        }
        startActivity(intent);
    }

    public void enableDriveButton(){
        if (flag1 && flag2){
            driveNow.setEnabled(true);
        }
    }

    public void createEntryForCommutor(){
        startedDrivingAt = new Date();
        if(FilePath!= null && mileage != null)
        {
            values.put("ODOMETER_IMAGE_URI",FilePath);
            values.put("STARTING_MILEAGE",mileage);
        }
        else{
            values.put("ODOMETER_IMAGE_URI","N/A");
            values.put("STARTING_MILEAGE","N/A");
        }

        values.put("STARTED_DRIVING_AT_TIME", startedDrivingAt.toString());

        try {
            db.insert("TrackCommuteInfo",null,values);
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void gotToS4(View view) // Start driving button onClick calls this
    {
        int cellValue = 0;
        createEntryForCommutor();
        Cursor cursor = db.rawQuery("SELECT ID FROM TrackCommuteInfo\n" +
                "WHERE ID = (SELECT MAX(id) FROM TrackCommuteInfo);", null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("ID");
            cellValue = cursor.getInt(columnIndex);

        }
        cursor.close();

        Intent intent = new Intent(this, S4_timeAndDistanceTravelled.class);

        intent.putExtra("ID", cellValue);
        intent.putExtra("Date",startedDrivingAt.getTime());
        intent.putExtra("Mileage",mileage);

        db.close();

        startActivity(intent);
    }


}