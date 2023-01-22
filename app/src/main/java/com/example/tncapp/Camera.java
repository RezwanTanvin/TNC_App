package com.example.tncapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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


public class Camera extends AppCompatActivity implements View.OnClickListener {

    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    Button picCapture,videoCapture;
    private  ImageCapture imageCapture;
    PreviewView previewView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        int permissionCheck = ContextCompat.checkSelfPermission(Camera.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        previewView = findViewById(R.id.previewView);
        picCapture = findViewById(R.id.image_capture_button);
        videoCapture = findViewById(R.id.video_capture_button);

        picCapture.setOnClickListener(this);
        videoCapture.setOnClickListener(this);

        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(()->{
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                startCameraX(cameraProvider);
            }

            catch (ExecutionException e){
                e.printStackTrace();
            }
            catch(InterruptedException e){
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
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector,preview,imageCapture);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_capture_button:
                capturePhoto();
                break;
            case R.id.video_capture_button:
                break;
        }
    }

    private void capturePhoto() {


        File photoDir = new File("/mnt/sdcard/Pictures/TNCData");

        if(!photoDir.exists()){
           photoDir.mkdir();
        }

        Date date = new Date();

        String timestamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + timestamp + ".jpg";

        File photofile = new File(photoFilePath);

        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(photofile).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(Camera.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(Camera.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });



    }


};
