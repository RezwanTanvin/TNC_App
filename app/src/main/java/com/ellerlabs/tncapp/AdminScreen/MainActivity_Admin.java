package com.ellerlabs.tncapp.AdminScreen;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ellerlabs.tncapp.AdminScreen.CommuteData.ChooseUser.ShowAndChooseUser;
import com.ellerlabs.tncapp.ContractorScreen.MainActivity;
import com.ellerlabs.tncapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity_Admin extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    ImageView trackCommuteData;
    private FirebaseAuth mAuth ;
    private FirebaseUser user;
    TextView userName, SwitchToContractor;

    Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mode_activity_main_admin);


        trackCommuteData = findViewById(R.id.trackCommuteImage2);
        trackCommuteData.setClipToOutline(true);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.track_commmute_logo);
        trackCommuteData.setImageBitmap(bitmap1);

        userName = findViewById(R.id.Hi2);
        SwitchToContractor = findViewById(R.id.SwitchToContractor);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        userName.setText("Hello\n" + user.getDisplayName());

        if(checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[]{
                    ACCESS_FINE_LOCATION,
                    ACCESS_BACKGROUND_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    ACCESS_WIFI_STATE,
                    CAMERA,
                    NOTIFICATION_SERVICE,
                    POST_NOTIFICATIONS,
                    AUDIO_SERVICE,
                    WRITE_EXTERNAL_STORAGE,
                    READ_EXTERNAL_STORAGE,
            }, PERMISSION_REQUEST_CODE);
        };

        SwitchToContractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MainActivity.class));
            }
        });

    }

    public void goToTrackCommuteS1(View view){
        startActivity(new Intent(mContext, ShowAndChooseUser.class));
    }
}