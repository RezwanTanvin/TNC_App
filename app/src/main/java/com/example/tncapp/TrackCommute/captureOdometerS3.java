package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tncapp.Camera;
import com.example.tncapp.R;

public class captureOdometerS3 extends AppCompatActivity {

    EditText mileage ;
    Button takePic;
    ImageView imageview;

    Button submitBtn;

    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_odometer_s3);

        mileage= findViewById(R.id.mileageAtStarteditText);
        takePic = findViewById(R.id.takepictuerofOdotmeterBtn);
        imageview = findViewById(R.id.imageView);
        submitBtn = findViewById(R.id.submitBtn1);
        submitBtn.setEnabled(false);
        takePic.setEnabled(false);
    }

    public void onStart(){
        super.onStart();

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

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    startActivity(new Intent(captureOdometerS3.this, Camera.class));

                }
            }


        });
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (allPermissionsGranted()) {
//                startCamera();
//            } else {
//                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }




    public void goToS2(View view) //This ic called by the submit button.
    {
        if(mileage != null) {
            mileage.getText().toString().trim();
        }

        Intent intent = new Intent(this,trackCommS2.class);
        intent.putExtra("Mileage",mileage.getText().toString().trim());
        startActivity(intent);
    }

}