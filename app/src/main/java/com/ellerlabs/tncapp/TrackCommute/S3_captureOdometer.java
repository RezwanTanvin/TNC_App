package com.ellerlabs.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ellerlabs.tncapp.Camera;
import com.ellerlabs.tncapp.ImagePreview;
import com.ellerlabs.tncapp.R;

public class S3_captureOdometer extends AppCompatActivity {

    EditText mileage ;
    Button takePic;
    ImageView thumbnail;

    Button submitBtn;

   Intent intent;
   String FilePath;
   String mileageFromCameraClass;
   TextView header;
   String Mileage;
    //private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_odometer_s3);

        mileage= findViewById(R.id.mileageAtStarteditText);
        takePic = findViewById(R.id.takepictuerofOdotmeterBtn);
        thumbnail = findViewById(R.id.imageView);
        submitBtn = findViewById(R.id.submitBtn1);
        submitBtn.setEnabled(false);
        takePic.setEnabled(false);
        header = findViewById(R.id.textView4);
        Mileage = "";


    }
    boolean fromS2,fromS6;
    String pathFrom;

    public void onStart(){
        super.onStart();

        fromS2 = false;
        fromS6 = false;

        intent = getIntent();

        pathFrom = intent.getStringExtra("pathFrom");
        if(pathFrom == "S6"){
            fromS6 = true;
        }
        else{
            fromS2 = true;
        }

        if ( intent.getStringExtra("FilePath")!= null)
        {
            FilePath = intent.getStringExtra("FilePath");
            thumbnail.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(FilePath);
            thumbnail.setEnabled(true);
            thumbnail.setImageBitmap(bitmap);
            header.setText("Confirm the following information");
            takePic.setVisibility(View.INVISIBLE);

        }
        else
        {
            FilePath = "";
            thumbnail.setEnabled(false);
        }

        if ( intent.getStringExtra("Mileage")!= null)
        {
            mileageFromCameraClass = intent.getStringExtra("Mileage");

            mileage.setText(mileageFromCameraClass);
            Mileage = mileageFromCameraClass;

            submitBtn.setEnabled(true);
        }


        mileage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mileage.getText().toString().isEmpty()) {
                    submitBtn.setEnabled(false);
                    takePic.setEnabled(false);
                }
                    else{
                        submitBtn.setEnabled(true);
                        takePic.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mileage.getText().toString().isEmpty()) {
                    submitBtn.setEnabled(true);
                    takePic.setEnabled(true);
                }
                else {
                    submitBtn.setEnabled(false);
                    takePic.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(S3_captureOdometer.this, ImagePreview.class);
                intent.putExtra("FilePath",FilePath);
                startActivity(intent);
            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {

                    if(mileage != null) {
                        Mileage = mileage.getText().toString().trim();
                    }

                    Intent intent = new Intent(S3_captureOdometer.this, Camera.class);

                    intent.putExtra("Mileage",Mileage);

                    startActivity(intent);

                }
            }


        });
    }






    public void goToS2orS6(View view) //This is called by the submit button.
    {
        if(mileage != null) {
            mileage.getText().toString().trim();
        }

        Intent intent;

        if(fromS2)
        {
            intent = new Intent(this, S2_collectVehicleTripInfo.class);
        }
        else
        {
            intent = new Intent(this, S6_collectCorrectOdometer.class);
        }

        intent.putExtra("Mileage",Mileage);
        intent.putExtra("FilePath",FilePath);
        startActivity(intent);

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please avoid pressing the back button, this will corrupt the data collected.", Toast.LENGTH_LONG).show();
    }

}