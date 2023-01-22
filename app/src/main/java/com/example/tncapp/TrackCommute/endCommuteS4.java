package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tncapp.R;

public class endCommuteS4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_commute_s4);
    }

    public void gotToS5(View view){
        startActivity(new Intent(this,mileageConfirmationS5.class));
    }
}