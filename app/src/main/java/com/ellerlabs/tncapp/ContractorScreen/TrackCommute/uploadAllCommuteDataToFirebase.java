package com.ellerlabs.tncapp.ContractorScreen.TrackCommute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ellerlabs.tncapp.ContractorScreen.MainActivity;
import com.ellerlabs.tncapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class uploadAllCommuteDataToFirebase extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageRef;
    DatabaseReference firebasedb;

    SQLiteDatabase SQLdb1,SQLdb2;

    TrackCommuteDataObj newObj;
    String imageStorageRef = "";

    ProgressBar progressBar1,progressBar2,progressBar3,progressBar4;
    Button mainMenuBtn,retryBtn;
    String customEmail;

    TextView tryCatchResultTextBox1, tryCatchResultTextBox2, tryCatchResultTextBox3, tryCatchResultTextBox4;
    boolean uploadTrackingSuccessFlag, uploadLocationDataSuccessFlag, connectionToFirebaseSuccessFlag, downloadFromSQL;

//TODO: Perhaps this entire class needs to be turned into a foreground activity class.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_commute_activity_upload_all_commute_data_to_firebase);

        //TODO : The message section for firebase does not really work.
        // It always says that its working. Except sometimes where it just keeps showing the progressbar circling.

        progressBar1 = findViewById(R.id.progressBar2);
        progressBar2 = findViewById(R.id.progressBar4);
        progressBar3 = findViewById(R.id.progressBar5);
        progressBar4 = findViewById(R.id.progressBar6);

        tryCatchResultTextBox1 = findViewById(R.id.tryCatchResultTextBox1);
        tryCatchResultTextBox2 = findViewById(R.id.tryCatchResultTextBox2);
        tryCatchResultTextBox3 = findViewById(R.id.tryCatchResultTextBox3);
        tryCatchResultTextBox4 = findViewById(R.id.tryCatchResultTextBox4);

        mainMenuBtn = findViewById(R.id.mainMenuBtn);
        retryBtn = findViewById((R.id.retryBtn));




    }
    @Override
    protected void onStart() {
        super.onStart();

        getEmailAndAmendIt();
        tryEstablishingConnectionToFirebase();
        loadDataFromLocalStorage();
        checkifuploadToFirebaseWasSuccessful();

        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(uploadAllCommuteDataToFirebase.this, MainActivity.class));
            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmailAndAmendIt();
                tryEstablishingConnectionToFirebase();
                loadDataFromLocalStorage();
                checkifuploadToFirebaseWasSuccessful();
            }
        });

    }

    private void checkifuploadToFirebaseWasSuccessful() {
        try {

            if (uploadTrackingSuccessFlag && uploadLocationDataSuccessFlag && connectionToFirebaseSuccessFlag && downloadFromSQL){
                Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.check_mark);
                progressBar4.setForeground(drawable);
                tryCatchResultTextBox4.setText("Upload was successful. Please use the button below to go to the main menu.");
                mainMenuBtn.setVisibility(View.VISIBLE);
            }
        }catch(Exception e){
            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.failed_x_sign);
            progressBar4.setForeground(drawable);
            retryBtn.setVisibility(View.VISIBLE);
            tryCatchResultTextBox4.setText("Error: " + e);
        }
    }

    // We are doing this so that we can store this as a firebast file path. Cannot use "." in file path but email contains this.
    private void getEmailAndAmendIt() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user.getEmail() != null){
            customEmail = user.getEmail();
        }else{
            customEmail = "NULL.";
        }
        int index = customEmail.lastIndexOf(".");
        customEmail = customEmail.substring(0, index) + "_" + customEmail.substring(index + 1);
    }

    private void tryEstablishingConnectionToFirebase() {
        try {
            database = FirebaseDatabase.getInstance();
            firebasedb = database.getReference("Commute Data");
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();

            connectionToFirebaseSuccessFlag = true;

            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.check_mark);
            progressBar1.setForeground(drawable);
            tryCatchResultTextBox1.setText("Establish Connection to Firebase : Task was successful.");
        }
        catch(Exception e){
            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.failed_x_sign);
            progressBar1.setForeground(drawable);
            retryBtn.setVisibility(View.VISIBLE);
            tryCatchResultTextBox1.setText("Error: " + e);
        }
    }




    
    void loadDataFromLocalStorage(){
        try {
            downloadCommuteDataFromSQLDatabase();
            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.check_mark);
            progressBar2.setForeground(drawable);
            tryCatchResultTextBox2.setText("Load Trip Info : Task successful.");
            downloadFromSQL = true ;
        }
        catch (Exception e){
            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.failed_x_sign);
            progressBar2.setForeground(drawable);
            mainMenuBtn.setVisibility(View.VISIBLE);
            tryCatchResultTextBox2.setText("Error: " + e);
            downloadFromSQL = false ;

        }
        try {
            downloadLocationTrackingDataFromSQLDatabase();
            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.check_mark);
            progressBar3.setForeground(drawable);
            tryCatchResultTextBox3.setText("Load GPS Coordinates : Task successful.");

            downloadFromSQL = true ;
        }
        catch(Exception e){
            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.failed_x_sign);
            progressBar3.setForeground(drawable);
            retryBtn.setVisibility(View.VISIBLE);
            tryCatchResultTextBox3.setText("Error: " + e);
            downloadFromSQL = false;
        }
    }


     String uploadImageToFirebase(String filePath, String fileTitle)  {

        StorageReference imagesRef = storageRef.
                child("Commute Data").
                child(user.getUid() + " - " + user.getDisplayName() + " - " + customEmail).
                child("Odometer_Readings").
                child(newObj.STARTED_DRIVING_AT_TIME).child(fileTitle);

       //Way to upload files.
        Uri file = Uri.fromFile(new File((filePath)));

        UploadTask uploadTask = imagesRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar4.setVisibility(View.INVISIBLE);
                tryCatchResultTextBox4.setText("Failed to upload. \n\nError Message: " + e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.check_mark);
                progressBar4.setForeground(drawable);
                tryCatchResultTextBox4.setText("Upload Images to Remote Server : Task successful.");
            }
        });

         return imagesRef.toString();
    }

    void downloadCommuteDataFromSQLDatabase(){
        SQLdb1 = openOrCreateDatabase("TrackCommuteInfo",MODE_PRIVATE,null);

        //We will store the entire table data into our custom built object arrayList.
        ArrayList<TrackCommuteDataObj> TrackCommuteDataObjArrayList = new ArrayList<>();

        Cursor cr = SQLdb1.rawQuery("SELECT * FROM TrackCommuteInfo",null);
        if(cr.moveToFirst()) {
            do {
                ArrayList<String> row = new ArrayList<>();

                for (int i = 0; i < cr.getColumnCount(); i++) {
                    row.add(cr.getString(i));
                }

                TrackCommuteDataObj TrackCommuteDataObj = new TrackCommuteDataObj(row);

                TrackCommuteDataObjArrayList.add(TrackCommuteDataObj);
            }
            while (cr.moveToNext());
        }
        cr.close();

        uploadCommuteDatatoFirebaseDB(TrackCommuteDataObjArrayList);

    }

    void uploadCommuteDatatoFirebaseDB(ArrayList<TrackCommuteDataObj> TrackCommuteDataObjArrayList){

        newObj = TrackCommuteDataObjArrayList.get(0);

        try{
            firebasedb = database.getReference("Commute Data").
                    child(user.getUid() + " - " + user.getDisplayName() + " - " + customEmail).
                    child(newObj.STARTED_DRIVING_AT_TIME).
                    child("Odometer_Readings");


            firebasedb.child("DRIVE_TIME").setValue(newObj.DRIVE_TIME);

            if (!newObj.ODOMETER_IMAGE_URI.contains("N/A"))
            {
                firebasedb.child("ODOMETER_IMAGE_URI").setValue(uploadImageToFirebase(newObj.ODOMETER_IMAGE_URI,"ODOMETER_IMAGE_URI"));
            }
            else
            {
                firebasedb.child("ODOMETER_IMAGE_URI").setValue("N/A");
            }

            firebasedb.child("OVERRIDE_MILEAGE").setValue(newObj.OVERRIDE_MILEAGE);

            if (!newObj.OVERRIDE_MILEAGE_URI.contains("N/A"))
            {
             firebasedb.child("OVERRIDE_MILEAGE_URI").setValue(uploadImageToFirebase(newObj.OVERRIDE_MILEAGE_URI,"OVERRIDE_MILEAGE_URI"));
            }
            else
            {
                firebasedb.child("OVERRIDE_MILEAGE_URI").setValue("N/A");
            }

            firebasedb.child("SQL_TABLE_NAME").setValue(newObj.SQL_TABLE_NAME);
            firebasedb.child("STARTED_DRIVING_AT_TIME").setValue(newObj.STARTED_DRIVING_AT_TIME);
            firebasedb.child("STARTING_MILEAGE").setValue(newObj.STARTING_MILEAGE);
            firebasedb.child("TOTAL_MILES_DRIVEN_FROM_GPS").setValue(newObj.TOTAL_MILES_DRIVEN_FROM_GPS);
            firebasedb.child("TRIP_TYPE").setValue(newObj.TRIP_TYPE);
            firebasedb.child("VEHICLE_TYPE").setValue(newObj.VEHICLE_TYPE);

            uploadTrackingSuccessFlag = true;
            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.check_mark);
            progressBar4.setForeground(drawable);
            tryCatchResultTextBox4.setText("Upload Trip Info : Task successful.");
            
        }
        catch (Exception e){
            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.failed_x_sign);
            progressBar4.setForeground(drawable);
            tryCatchResultTextBox4.setText("Error: " + e);
        }



    }

    void downloadLocationTrackingDataFromSQLDatabase(){
        SQLdb2 = openOrCreateDatabase("location_db",MODE_PRIVATE,null);

        //We will store the entire table data into our custom built object arrayList.
        ArrayList<LocationDataObj> LocationDataObjArrayList = new ArrayList<>();

        Cursor cr = SQLdb2.rawQuery("SELECT * FROM permLocationTable_1",null);
        if(cr.moveToFirst()) {
            do {
                ArrayList<String> row = new ArrayList<>();

                for (int i = 0; i < cr.getColumnCount(); i++) {
                    row.add(cr.getString(i));
                }

                LocationDataObj LocationDataObj = new LocationDataObj(row);
                LocationDataObjArrayList.add(LocationDataObj);
            }
            while (cr.moveToNext());
        }
        cr.close();

        SQLdb2.execSQL("drop table IF EXISTS  permLocationTable_1");

        uploadLocationTrackingDataToFirebaseDB(LocationDataObjArrayList);

    }

    void uploadLocationTrackingDataToFirebaseDB(ArrayList<LocationDataObj> LocationDataObjArrayList){

        firebasedb = database.getReference("Commute Data").
                child(user.getUid() + " - " + user.getDisplayName() + " - " + customEmail).
                child(newObj.STARTED_DRIVING_AT_TIME).
                child("Location_Data");

        int ID = 0;
        try {
            for (LocationDataObj newObj : LocationDataObjArrayList
            ) {
                ID++;
                firebasedb.child(String.valueOf(ID)).child("latitude").setValue(newObj.latitude);
                firebasedb.child(String.valueOf(ID)).child("longitude").setValue(newObj.longitude);
                firebasedb.child(String.valueOf(ID)).child("gpsStrength").setValue(newObj.gpsStrength);
            }
            uploadLocationDataSuccessFlag = true;

            Drawable drawable = ContextCompat.getDrawable(uploadAllCommuteDataToFirebase.this, R.drawable.check_mark);
            progressBar4.setForeground(drawable);
            tryCatchResultTextBox4.setText("Upload GPS Coordinates : Task successful.");


        }
        catch(Exception e){
            progressBar4.setVisibility(View.INVISIBLE);
                tryCatchResultTextBox4.setText("Failed to upload. \n\nError Message: " + e);
        }

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please avoid pressing the back button, this will corrupt the data collected.", Toast.LENGTH_LONG).show();
    }


}
