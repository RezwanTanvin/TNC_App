package com.ellerlabs.tncapp.TrackCommute;


import static com.ellerlabs.tncapp.TrackCommute.S4_S_distanceCalculator.distance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ellerlabs.tncapp.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class S4_timeAndDistanceTravelled extends AppCompatActivity {

    TextView timer,showDistance, GPSStrength;
    long date;
    boolean timerRunning;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_time_distance_s4);

        timer = findViewById(R.id.timeTV);
        showDistance = findViewById(R.id.DistanceTV);
        GPSStrength = findViewById(R.id.title4);

        date = new Date().getTime();
        timerRunning = true;

        intent = new Intent(this, S4_S_distanceCalculator.class);
        startService(intent);

    }


    public void onStart(){
        super.onStart();
        showLiveClockandDistance(timer);

    }

    public void showLiveClockandDistance(final TextView timer) {

        final Handler handler = new Handler();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsedTime = System.currentTimeMillis() - date;
                int seconds = (int) (elapsedTime / 1000) % 60;
                int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
                int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);

                String timeElapsed = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timer.setText(timeElapsed);

                Double distance = S4_S_distanceCalculator.getDistance();

                DecimalFormat df = new DecimalFormat("#.##");
                showDistance.setText(String.valueOf(df.format(distance)) + " KM");


                if (timerRunning) {
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    public void goToS5(View view){

        timerRunning= false;
        stopService(intent);

        startActivity(new Intent(S4_timeAndDistanceTravelled.this, googelMapsData.class));


    }


}