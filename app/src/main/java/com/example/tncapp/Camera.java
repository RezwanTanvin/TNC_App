package com.example.tncapp;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tncapp.TrackCommute.trackCommS2;
import com.example.tncapp.databinding.ActivityMainBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.core.Preview;
import androidx.camera.core.CameraSelector;
import android.util.Log;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.video.FallbackStrategy;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.LifecycleOwner;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.os.Bundle;
import android.Manifest;
import android.net.Uri;
import android.widget.ImageView;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;



public class Camera extends AppCompatActivity implements View.OnClickListener {

    static final int PERMISSION_REQUEST_CODE = 200 ;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    Button picCapture,flashToogle;
    private  ImageCapture imageCapture;
    PreviewView previewView;
    Intent intent;
    String Mileage;
    TextView flashLabel;


    @RequiresApi(api = Build.VERSION_CODES.Q)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        picCapture = findViewById(R.id.image_capture_button);
        //videoCapture = findViewById(R.id.video_capture_button);
        flashToogle = findViewById(R.id.flashToogle);
        flashLabel = findViewById(R.id.flashOnOff);

        picCapture.setOnClickListener(this);
        flashToogle.setOnClickListener(this);
        flashLabel.setOnClickListener(this);

        intent = getIntent();

        if ( intent.getStringExtra("Mileage")!= null) {
            Mileage = intent.getStringExtra("Mileage");
        }

            //videoCapture.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    ACCESS_FINE_LOCATION,
                    ACCESS_BACKGROUND_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    ACCESS_WIFI_STATE,
                    CAMERA,
                    AUDIO_SERVICE,
                    WRITE_EXTERNAL_STORAGE,
                    READ_EXTERNAL_STORAGE,
                    }, PERMISSION_REQUEST_CODE);
        };



        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(()->{

            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                startCameraX(cameraProvider);
            }

            catch (ExecutionException | InterruptedException e){
                e.printStackTrace();
            }

        }, getExecutor());


    }




    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build();

        cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture);

       imageCapture.setFlashMode(ImageCapture.FLASH_MODE_AUTO);
        flashLabel.setText("Auto Flash");



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_capture_button:
                capturePhoto();
                break;
           // case R.id.video_capture_button:
               // break;
            case R.id.flashToogle :
                if (imageCapture.getFlashMode() == ImageCapture.FLASH_MODE_ON)
                {
                    imageCapture.setFlashMode(ImageCapture.FLASH_MODE_OFF);
                    flashLabel.setText("Flash OFF");

                } else {
                    imageCapture.setFlashMode(ImageCapture.FLASH_MODE_ON);
                    flashLabel.setText("Flash ON");
                }
            case R.id.flashOnOff:
                if (imageCapture.getFlashMode() == ImageCapture.FLASH_MODE_ON)
                {
                    imageCapture.setFlashMode(ImageCapture.FLASH_MODE_OFF);
                    flashLabel.setText("Flash OFF");

                } else {
                    imageCapture.setFlashMode(ImageCapture.FLASH_MODE_ON);
                    flashLabel.setText("Flash ON");
                }
        }
    }

    private void capturePhoto() {


        File photoDir = new File(Environment.getExternalStorageDirectory() + "/Pictures/TNC_App");


        if(!photoDir.exists())
        {
           photoDir.mkdirs();
        }

        Date date = new Date();

        String timestamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + timestamp + ".jpg";

        File photoFile = new File(photoFilePath);

        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(Camera.this, "Image saved successfully", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(Camera.this, com.example.tncapp.TrackCommute.captureOdometerS3.class);
                        intent.putExtra("FilePath",photoFilePath);
                        intent.putExtra("Mileage", Mileage);
                        startActivity(intent);

                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        //Toast.makeText(Camera.this, exception.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(Camera.this, "Image failed to save. Error : " + exception.toString() , Toast.LENGTH_SHORT).show();

                    }
                });

    }



};