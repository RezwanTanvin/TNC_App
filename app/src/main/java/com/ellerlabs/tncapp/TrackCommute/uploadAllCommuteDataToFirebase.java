package com.ellerlabs.tncapp.TrackCommute;

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

import com.ellerlabs.tncapp.MainActivity;
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

    ProgressBar progressBar;
    TextView txtViewMsg;
    Button mainMenuBtn;

    boolean flag1,flag2,flag3;

//TODO: Perhaps this entire class needs to be turned into a foreground activity class.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_all_commute_data_to_firebase);
        //TODO : The message section for firebase does not really work.
        // It always says that its working. Except sometimes where it just keeps showing the progressbar circling.

        progressBar = findViewById(R.id.progressBar2);
        txtViewMsg = findViewById(R.id.textView19);
        mainMenuBtn = findViewById(R.id.mainMenuBtn);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();

        try {
            firebasedb = database.getReference("Commute Data").child(user.getUid());
            flag3 = true;
        }
        catch(Exception e){
            Drawable drawable = ContextCompat.getDrawable(com.ellerlabs.tncapp.TrackCommute.uploadAllCommuteDataToFirebase.this, R.drawable.check_mark);
            progressBar.setForeground(drawable);
            mainMenuBtn.setVisibility(View.VISIBLE);
            txtViewMsg.setText("Error: " + e);
        }

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

         downloadCommuteDataFromSQLDatabase();
         downloadLocationTrackingDataFromSQLDatabase();

         if (flag1 && flag2 && flag3){
             Drawable drawable = ContextCompat.getDrawable(com.ellerlabs.tncapp.TrackCommute.uploadAllCommuteDataToFirebase.this, R.drawable.check_mark);
             progressBar.setForeground(drawable);
             txtViewMsg.setText("Upload was succesfully. Please use the button below to go to the main menu.");
             mainMenuBtn.setVisibility(View.VISIBLE);
         }
        }catch(Exception e){
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
        }

        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.ellerlabs.tncapp.TrackCommute.uploadAllCommuteDataToFirebase.this, MainActivity.class));
            }
        });
    }


     String uploadImageToFirebase(String filePath, String fileTitle)  {

        StorageReference imagesRef = storageRef.child("Commute Data").child(user.getUid()).child("Odometer_Readings").child(newObj.STARTED_DRIVING_AT_TIME).child(fileTitle);

       //Way to upload files.
        Uri file = Uri.fromFile(new File((filePath)));

        UploadTask uploadTask = imagesRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                txtViewMsg.setText("Failed to upload. \n\nError Message: " + e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

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

                com.ellerlabs.tncapp.TrackCommute.TrackCommuteDataObj TrackCommuteDataObj = new com.ellerlabs.tncapp.TrackCommute.TrackCommuteDataObj(row);

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
            firebasedb = database.getReference("Commute Data").child(user.getUid()).child(newObj.STARTED_DRIVING_AT_TIME);

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

            flag1 = true;
        }
        catch (Exception e){
           progressBar.setVisibility(View.INVISIBLE);
           txtViewMsg.setText("Failed to upload. \n\nError Message: " + e);
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

                com.ellerlabs.tncapp.TrackCommute.LocationDataObj LocationDataObj = new com.ellerlabs.tncapp.TrackCommute.LocationDataObj(row);
                LocationDataObjArrayList.add(LocationDataObj);
            }
            while (cr.moveToNext());
        }
        cr.close();

        SQLdb2.execSQL("drop table IF EXISTS  permLocationTable_1");

        uploadLocationTrackingDataToFirebaseDB(LocationDataObjArrayList);

    }

    void uploadLocationTrackingDataToFirebaseDB(ArrayList<LocationDataObj> LocationDataObjArrayList){

        firebasedb = database.getReference("Commute Data").child(user.getUid()).child(newObj.STARTED_DRIVING_AT_TIME).child("Location_Data");
        int ID = 0;
        try {
            for (LocationDataObj newObj : LocationDataObjArrayList
            ) {
                ID++;
                firebasedb.child(String.valueOf(ID)).child("latitude").setValue(newObj.latitude);
                firebasedb.child(String.valueOf(ID)).child("longitude").setValue(newObj.longitude);
                firebasedb.child(String.valueOf(ID)).child("gpsStrength").setValue(newObj.gpsStrength);
            }
            flag2 = true;
        }
        catch(Exception e){
            progressBar.setVisibility(View.INVISIBLE);
            if (!txtViewMsg.getText().toString().contains("Failed")){
                txtViewMsg.setText("Failed to upload. \n\nError Message: " + e);
            }
            else{
                txtViewMsg.setText(txtViewMsg.getText().toString().contains("Failed") + "\n\nError Message: " + e);
            }
        }

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please avoid pressing the back button, this will corrupt the data collected.", Toast.LENGTH_LONG).show();
    }


}
