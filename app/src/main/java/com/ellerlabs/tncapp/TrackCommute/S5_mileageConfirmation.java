package com.ellerlabs.tncapp.TrackCommute;

import static com.ellerlabs.tncapp.TrackCommute.S4_S_distanceCalculator.distance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ellerlabs.tncapp.MainActivity;
import com.ellerlabs.tncapp.R;

import java.text.DecimalFormat;
import android.database.sqlite.SQLiteDatabase;


public class S5_mileageConfirmation extends AppCompatActivity {

    TextView showMileage;
    Button yes,no;
    Intent intent;
    Double mileage;
    int id;

    private SQLiteDatabase mSQLiteDatabase;
    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mileage_confirmation_s5);

        intent = new Intent();
        intent = getIntent();

        id = intent.getIntExtra("ID",0);
        mileage = intent.getDoubleExtra("New_Mileage",0);
        DecimalFormat df = new DecimalFormat("#.##");

        showMileage = findViewById(R.id.textView13);
        yes = findViewById(R.id.yesBtn2);
        no = findViewById(R.id.noBtn2);

        showMileage.setText(df.format(mileage));

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 mSQLiteDatabase = com.ellerlabs.tncapp.TrackCommute.S5_mileageConfirmation.this.openOrCreateDatabase("TrackCommuteInfo", Context.MODE_PRIVATE, null);

                values = new ContentValues();
                values.put("OVERRIDE_MILEAGE",  "N/A");
                values.put("OVERRIDE_MILEAGE_URI","N/A  : GPS Calculated Data Correctly");


                values.put("SQL_TABLE_NAME", "permLocationTable_1");

                mSQLiteDatabase.update("TrackCommuteInfo",values,"ID = ?",new String[]{String.valueOf(id)});

                startActivity(new Intent(com.ellerlabs.tncapp.TrackCommute.S5_mileageConfirmation.this,
                        com.ellerlabs.tncapp.TrackCommute.uploadAllCommuteDataToFirebase.class));
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToS6();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void goToS6(){

        intent = new Intent (this, S6_collectCorrectOdometer.class);
        intent.putExtra("ID",id);

        startActivity(intent);
    }

    public void goToMainScreen(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please avoid pressing the back button, this will corrupt the data collected.", Toast.LENGTH_LONG).show();
    }
}