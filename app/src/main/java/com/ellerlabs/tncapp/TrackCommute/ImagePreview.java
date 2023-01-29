package com.ellerlabs.tncapp.TrackCommute;

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


public class ImagePreview extends AppCompatActivity {
    Intent intent;
    String FilePath;
    ImageView image;
    ImageButton iButton;
    float mScaleFactor = 1.0f;
    ScaleGestureDetector mScaleGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);

        image = findViewById(R.id.imageView3);
        iButton = findViewById(R.id.imageButton);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        intent = getIntent();

        if ( intent.getStringExtra("FilePath")!= null) {
            FilePath = intent.getStringExtra("FilePath");
            Bitmap bitmap = BitmapFactory.decodeFile(FilePath);
            image.setImageBitmap(bitmap);
        }


        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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