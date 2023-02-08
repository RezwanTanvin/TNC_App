package com.ellerlabs.tncapp.TrackCommute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ellerlabs.tncapp.Camera;
import com.ellerlabs.tncapp.ImagePreview;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class S6_collectCorrectOdometer extends AppCompatActivity {

    TextView dataEntry,header;

    Button takePic, submit;

    Intent intent;

    int id;

    private SQLiteDatabase db;

    String manualInputMileage, manualInputMileageString, filepath ;

    ContentValues values;

    ImageView thumbnail;

    int rowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_correct_odometer_s6);

        intent = new Intent();
        intent = getIntent();





        submit = findViewById(R.id.submitBtn);
        takePic = findViewById(R.id.takepictuerofOdotmeterBtn2);
        dataEntry = findViewById(R.id.mileageAtStarteditText2);
        thumbnail = findViewById(R.id.imageView4);
        header = findViewById(R.id.textView14);
        submit.setEnabled(false);
        takePic.setEnabled(false);


    }

    @Override
    protected void onStart() {
        super.onStart();

        rowID = intent.getIntExtra("ID",0);
        dataEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (dataEntry.getText().toString().isEmpty()) {
                    submit.setEnabled(false);
                    takePic.setEnabled(false);
                }
                else{
                    submit.setEnabled(true);
                    takePic.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!dataEntry.getText().toString().isEmpty()) {
                    submit.setEnabled(true);
                    takePic.setEnabled(true);
                }
                else {
                    submit.setEnabled(false);
                    takePic.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manualInputMileage = dataEntry.getText().toString().trim();

                Intent intent = new Intent(S6_collectCorrectOdometer.this, Camera.class);

                intent.putExtra("PathFrom","S6");
                intent.putExtra("Mileage",manualInputMileage);
                intent.putExtra("ID",rowID);


                startActivity(intent);
            }
        });

        if ((intent.getStringExtra("FilePath")!= null) && (intent.getStringExtra("Mileage") != null) )
        {
                filepath = intent.getStringExtra("FilePath");
                manualInputMileageString = intent.getStringExtra("Mileage");
                dataEntry.setText(manualInputMileageString);
        }

        //TODO: Add ability for user to click on picture and choose to zoom in and out like the other screen.
        if ( intent.getStringExtra("FilePath")!= null)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(filepath);
            thumbnail.setEnabled(true);
            thumbnail.setImageBitmap(bitmap);
            header.setText("Confirm new odometer reading");
            takePic.setVisibility(View.INVISIBLE);

        }

        values = new ContentValues();

        values.put("OVERRIDE_MILEAGE", manualInputMileageString);
        values.put("OVERRIDE_MILEAGE_URI", filepath);

        db = this.openOrCreateDatabase("TrackCommuteInfo", Context.MODE_PRIVATE, null);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   db.update("TrackCommuteInfo",values,"ID = ?",new String[]{String.valueOf(rowID)});

                Toast.makeText(S6_collectCorrectOdometer.this, "Saved successfully. Thank you for logging your commute.", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(com.ellerlabs.tncapp.TrackCommute.S6_collectCorrectOdometer.this, com.ellerlabs.tncapp.TrackCommute.uploadAllCommuteDataToFirebase.class));

               }
               catch(Exception e)
               {
                   Toast.makeText(S6_collectCorrectOdometer.this, "Error Message: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please avoid pressing the back button, this will corrupt the data collected.", Toast.LENGTH_LONG).show();
    }

}