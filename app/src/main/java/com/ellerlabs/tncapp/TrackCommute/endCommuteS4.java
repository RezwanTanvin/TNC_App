package com.ellerlabs.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ellerlabs.tncapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class endCommuteS4 extends AppCompatActivity {

    TextView timer;
    long date ;

    boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_commute_s4);

        timer = findViewById(R.id.timeTV);
        date = new Date().getTime();
        timerRunning = true;
        showLiveClock(timer);

    }


    public void showLiveClock(final TextView timer) {
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

                if (timerRunning) {
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    public void goToS5(View view){

        timerRunning= false;
        startActivity(new Intent(this,mileageConfirmationS5.class));

    }
}