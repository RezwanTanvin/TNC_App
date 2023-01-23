package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.tncapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class endCommuteS4 extends AppCompatActivity {

    TextView timer;
    long date ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_commute_s4);

        timer = findViewById(R.id.timeTV);
        date = new Date().getTime();

        showLiveClock(timer);

    }


    public void showLiveClock(final TextView clockTextView) {
        final Handler handler = new Handler();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        handler.post(new Runnable() {
            @Override
            public void run() {
                clockTextView.setText(dateFormat.format(new Date()));
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void goToS5(View view){
        startActivity(new Intent(this,mileageConfirmationS5.class));
    }
}