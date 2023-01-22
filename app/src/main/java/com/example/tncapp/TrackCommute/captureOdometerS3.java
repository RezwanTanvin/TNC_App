package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tncapp.R;

public class captureOdometerS3 extends AppCompatActivity {

    EditText mileage ;
    Button takePic;

    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_odometer_s3);

        mileage= findViewById(R.id.mileageAtStarteditText);
        takePic = findViewById(R.id.takepictuerofOdotmeterBtn);

    }

    public void onStart(){
        super.onStart();

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {

                }


            }


        });
    }

    public void goToS2(View view) //This ic called by the submit button.
    {
        if(mileage != null) {
            mileage.getText().toString().trim();
        }



        startActivity(new Intent(this,trackCommS2.class));
    }

}