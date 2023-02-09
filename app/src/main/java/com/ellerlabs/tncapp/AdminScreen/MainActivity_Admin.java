package com.ellerlabs.tncapp.AdminScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.ellerlabs.tncapp.R;

public class MainActivity_Admin extends AppCompatActivity {

    ImageView imageView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        imageView1 = findViewById(R.id.trackCommuteImage2);
        imageView1.setClipToOutline(true);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.track_commmute_logo);
        imageView1.setImageBitmap(bitmap1);



    }
}