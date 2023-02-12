package com.ellerlabs.tncapp.AdminScreen.CommuteData.ImagePreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ellerlabs.tncapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImagePreview extends AppCompatActivity {
    Intent intent;
    String URI;
    ImageView image;
    ImageButton backButton,rotateImageBtn;
    float mScaleFactor = 2.0f;
    ScaleGestureDetector mScaleGestureDetector;

    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mode_comm_image_preview);

        image = findViewById(R.id.imageView10);
        rotateImageBtn = findViewById(R.id.rotateImageBtn20);
        backButton = findViewById(R.id.imageButton20);

        mScaleGestureDetector = new ScaleGestureDetector(this, new com.ellerlabs.tncapp.AdminScreen.CommuteData.ImagePreview.ImagePreview.ScaleListener());

        intent = getIntent();
        URI = intent.getStringExtra("URI");
        storage = FirebaseStorage.getInstance();

        StorageReference gsReference = storage.getReferenceFromUrl(replacePercentageSigns(URI));

        final long TEN_MEGABYTE = 10024 * 10024;
        gsReference.getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                image.setImageBitmap(bitmap);
            }
        });

        rotateImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image.getRotation() == 90f){
                    image.setRotation(0f);
                }else{
                    image.setRotation(90f);
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
    public static String replacePercentageSigns(String str) {
        String temp = str.replaceAll("%20", " ");
        temp = temp.replaceAll("%40", "@");
        temp = temp.replaceAll("%3A", ":");
        return temp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            image.setScaleX(mScaleFactor);
            image.setScaleY(mScaleFactor);
            return true;
        }
    }
}