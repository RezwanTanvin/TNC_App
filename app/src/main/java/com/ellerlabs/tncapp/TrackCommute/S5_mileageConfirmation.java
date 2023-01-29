package com.ellerlabs.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ellerlabs.tncapp.MainActivity;
import com.ellerlabs.tncapp.R;

public class S5_mileageConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mileage_confirmation_s5);
    }

    public void goToS6(View view){
        startActivity(new Intent(this, S6_collectCorrectOdometer.class));
    }

    public void goToMainScreen(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
}