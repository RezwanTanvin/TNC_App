package com.ellerlabs.tncapp.ContractorScreen.HarvestProduct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ellerlabs.tncapp.R;

public class S2_preHarvest_SafetyCheckAndInspection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s2_pre_harvest_safety_check_and_inspection);
    }

    public void goToS3(View view) {

        startActivity(new Intent(this,S3_preHarvest_SafetyCheckAndEscapePlan.class));
    }


}