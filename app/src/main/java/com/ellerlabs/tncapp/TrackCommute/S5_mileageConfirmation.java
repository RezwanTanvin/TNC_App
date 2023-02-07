package com.ellerlabs.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ellerlabs.tncapp.MainActivity;
import com.ellerlabs.tncapp.R;

import java.text.DecimalFormat;


public class S5_mileageConfirmation extends AppCompatActivity {

    TextView showMileage;
    Button yes,no;
    Intent intent;
    Double mileage;
    int id;

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
                Toast.makeText(S5_mileageConfirmation.this, "Saved successfully. Thank you for logging your commute.", Toast.LENGTH_SHORT).show();
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
}