package com.example.tncapp.TrackCommute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.tncapp.R;


public class ImagePreview extends AppCompatActivity {
    Intent intent;
    String FilePath;
    ImageView image;
    ImageButton iButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);

        image = findViewById(R.id.imageView3);
        iButton = findViewById(R.id.imageButton);
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
}