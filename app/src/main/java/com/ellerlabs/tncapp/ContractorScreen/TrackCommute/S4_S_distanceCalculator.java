package com.ellerlabs.tncapp.ContractorScreen.TrackCommute;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.ellerlabs.tncapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;



public class S4_S_distanceCalculator extends Service {

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private SQLiteDatabase mSQLiteDatabase;
    static double distance;
    double tempDistance;
    double lati1;
    double loni1;
    double lati2;
    double loni2;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    DatabaseReference db;




    @Override
    public void onCreate() {

        distance = 0.0;
        lati1 = 0.0;


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        db = database.getReference("Commute Data").child(user.getUid()).child("GPS Data");
        db.removeValue();

    }


    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();
        startForeground(1,builder());

        LocationTracker(S4_S_distanceCalculator.this);
        startTracking();
        getLocationListener();

        Toast.makeText(this, "GPS Tracking Active", Toast.LENGTH_SHORT).show();


        return START_STICKY;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ServiceChannel";
            String description = "forGPSService";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyGPS", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification builder (){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"notifyGPS")
                .setSmallIcon(R.drawable.sunny_logo)
                .setContentTitle("Track Commute : Active")
                .setContentText("GPS Data collection : Active")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your location is being tracked in the backgroung for use in calculating distance to job site. "))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    private void LocationTracker(Context context) {

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mSQLiteDatabase = this.openOrCreateDatabase("location_db", Context.MODE_PRIVATE, null);
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

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    //Unable to ask permission from here. I dont know how

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

                        }
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
                    Toast.makeText(S4_S_distanceCalculator.this, "Location Services turned off, please enable location services to continue using app.", Toast.LENGTH_SHORT).show();
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



// using this to send back the distance calculated thus far.


    public static double getDistance() {
        return distance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       mLocationManager.removeUpdates(mLocationListener);

    }




}