package com.ellerlabs.tncapp.ContractorScreen.HarvestProduct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ellerlabs.tncapp.R;

public class S1_preHarvest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s1_pre_harvest);


    }

    public void goToS2(View view) {

        startActivity(new Intent(this,S2_preHarvest_SafetyCheckAndInspection.class));
    }
}