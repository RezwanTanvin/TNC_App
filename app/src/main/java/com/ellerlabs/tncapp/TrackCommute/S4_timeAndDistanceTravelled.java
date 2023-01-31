package com.ellerlabs.tncapp.TrackCommute;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.widget.TextView;

import com.ellerlabs.tncapp.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import java.util.List;


public class S4_timeAndDistanceTravelled extends AppCompatActivity {

    TextView timer,showDistance, GPSStrength;
    long date;

    boolean timerRunning;

    private Context mContext;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private SQLiteDatabase mSQLiteDatabase;
    double distance,tempDistance, lati1, loni1,  lati2,  loni2;

    private PowerManager.WakeLock wakeLock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_time_distance_s4);

        timer = findViewById(R.id.timeTV);
        showDistance = findViewById(R.id.DistanceTV);
        GPSStrength = findViewById(R.id.title4);
        distance = 0.0;
        lati1 = 0.0;

        date = new Date().getTime();
        timerRunning = true;
        mContext = this;


        LocationTracker(mContext);
        startTracking();

       // keepScreenOn(this);

    }

//
//
//    public void keepScreenOn(Context context) {
//        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, "myapp:wakeyy");
//
//        wakeLock.acquire();
//    }
//
//    public void releaseScreenOn() {
//        if (wakeLock != null) {
//            wakeLock.release();
//        }
//    }



    public void onStart(){
        super.onStart();

        showLiveClock(timer);
        getLocationListener();

    }

    public void LocationTracker(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mSQLiteDatabase = mContext.openOrCreateDatabase("location_db", Context.MODE_PRIVATE, null);
        createTable();
    }

    private void createTable() {

        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS  locations ");

        mSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS locations (" +
                "latitude DOUBLE, " +
                "longitude DOUBLE, " +
                "gpsStrength DOUBLE)");
    }

    public void startTracking() {
        if (mLocationManager != null) {
            List<String> providers = mLocationManager.getProviders(true);
            for (String provider : providers) {

                if (ActivityCompat.checkSelfPermission(S4_timeAndDistanceTravelled.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(S4_timeAndDistanceTravelled.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
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

                mLocationManager.requestLocationUpdates(provider, 2000, 50, getLocationListener());
            }
        }
    }

    private LocationListener getLocationListener() {
        if (mLocationListener == null) {
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null)
                    {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        if (location.getAccuracy() < 30.0)
                        {
                            storeLocation(latitude, longitude,location.getAccuracy());

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

                            DecimalFormat df = new DecimalFormat("#.##");

                            showDistance.setText(String.valueOf(df.format(distance)) + " KM");
                        }

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
                    Toast.makeText(mContext, "Location Services turned off, please enable location services to continue using app.", Toast.LENGTH_SHORT).show();
                }
            };
        }
        return mLocationListener;
    }

    private void storeLocation(double latitude, double longitude,double accuracy) {
        try {
            mSQLiteDatabase.execSQL("INSERT INTO locations (latitude, longitude, gpsStrength) VALUES ('" + latitude + "', '" + longitude + "', '" + accuracy + "');");

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
        startActivity(new Intent(S4_timeAndDistanceTravelled.this, googelMapsData.class));


    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        Intent intent = new Intent(this, MyService.class);
//     //   intent.putExtra("distance",distance);
//     //   intent.putExtra("time",date);
//        startService(intent);
//
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Intent intentFromForeGround = getIntent();
//        if (intentFromForeGround != null)
//        {
//            distance = intentFromForeGround.getDoubleExtra("distance", 0.0);
//            date = intentFromForeGround.getLongExtra("date", 0);
//        }
//    }

}