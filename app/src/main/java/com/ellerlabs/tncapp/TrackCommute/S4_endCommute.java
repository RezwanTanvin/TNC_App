package com.ellerlabs.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ellerlabs.tncapp.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import java.util.List;


public class S4_endCommute extends AppCompatActivity {

    TextView timer,showDistance, GPSStrength;
    long date;

    boolean timerRunning;

    private Context mContext;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private SQLiteDatabase mSQLiteDatabase;

    double distance,tempDistance;

    double lati1, loni1,  lati2,  loni2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_commute_s4);

        timer = findViewById(R.id.timeTV);
        showDistance = findViewById(R.id.DistanceTV);
        GPSStrength = findViewById(R.id.title4);
        distance = 0.0;
        lati1 = 0.0;

        date = new Date().getTime();
        timerRunning = true;
        showLiveClock(timer);

        mContext = this;

        LocationTracker(mContext);

    }

    public void onStart(){
        super.onStart();

        startTracking();
        getLocationListener();

    }

    public void LocationTracker(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mSQLiteDatabase = mContext.openOrCreateDatabase("location_db", Context.MODE_PRIVATE, null);
        createTable();
    }

    private void createTable() {

        mSQLiteDatabase.execSQL("DROP TABLE locations ");

        mSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS locations (" +
                "latitude DOUBLE, " +
                "longitude DOUBLE)");
    }

    public void startTracking() {
        if (mLocationManager != null) {
            List<String> providers = mLocationManager.getProviders(true);
            for (String provider : providers) {

                if (ActivityCompat.checkSelfPermission(S4_endCommute.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(S4_endCommute.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                mLocationManager.requestLocationUpdates(provider, 1000, 50, getLocationListener());
            }
        }
    }



    private LocationListener getLocationListener() {
        if (mLocationListener == null) {
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        storeLocation(latitude, longitude);

                        if (lati1 == 0.0)
                        {
                            lati1 = latitude;
                            loni1 = longitude;
                        }
                        else{
                            lati2 = latitude;
                            loni2 = longitude;

                            tempDistance = distanceBetweenTwoCoordinates(lati1,loni1,lati2,loni2);

                            lati1 = lati2;
                            loni1 = loni2;

                        }
                        if  (!(Double.isNaN(tempDistance)))
                        {
                            distance = distance + tempDistance;
                        }
                        DecimalFormat df = new DecimalFormat("#.###");
                        showDistance.setText(String.valueOf(df.format(distance)) + " KM");

                        GPSStrength.setText("GPS Strength: " + location.getAccuracy());
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };
        }
        return mLocationListener;
    }

    private void storeLocation(double latitude, double longitude) {
        try {
            mSQLiteDatabase.execSQL("INSERT INTO locations (latitude, longitude) VALUES ('" + latitude + "', '" + longitude + "');");
            //Toast.makeText(mContext, "Location stored in database", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void stopTracking() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(getLocationListener());
        }
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
    public static double distanceBetweenTwoCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public void goToS5(View view){

        timerRunning= false;
        stopTracking();
        startActivity(new Intent(S4_endCommute.this, googelMapsData.class));

    }
}