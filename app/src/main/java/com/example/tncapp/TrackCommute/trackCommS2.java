package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tncapp.R;

public class trackCommS2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_comm_s2);
    }

    public void gotToS3(View view){
        startActivity(new Intent(this,captureOdometerS3.class));
    }

    public void gotToS4(View view){

        startActivity(new Intent(this,endCommuteS4.class));
    }

}