package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tncapp.R;

public class captureOdometerS3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_odometer_s3);
    }

    public void goToS4(View view){
        startActivity(new Intent(this,endCommuteS4.class));
    }

}