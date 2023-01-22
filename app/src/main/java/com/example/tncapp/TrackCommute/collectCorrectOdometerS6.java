package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tncapp.MainActivity;
import com.example.tncapp.R;

public class collectCorrectOdometerS6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_correct_odometer_s6);
    }

    public void goToMainScreen(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
}