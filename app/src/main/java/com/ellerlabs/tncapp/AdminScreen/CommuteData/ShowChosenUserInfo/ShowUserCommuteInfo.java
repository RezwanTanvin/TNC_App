package com.ellerlabs.tncapp.AdminScreen.CommuteData.ShowChosenUserInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.ellerlabs.tncapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowUserCommuteInfo extends AppCompatActivity {

    Intent intent;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase fdb;
    DatabaseReference fdbRef;

    ImageButton backButton;

    AdapterForSUC adapter;

    RecyclerView rv;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mode_show_user_commute_info);

        intent = getIntent();
        String key = intent.getStringExtra("Key");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fdb = FirebaseDatabase.getInstance();
        fdbRef = fdb.getReference("Commute Data").child(key);

        backButton = findViewById(R.id.backButtonSUCI);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        intent = getIntent();
        key = intent.getStringExtra("Key");

        makeLayoutFullScreen();

        ArrayList<EventInfoObjectForCommUser> DataArrayList = new ArrayList<>();
        fdbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataArrayList.clear();
                for (DataSnapshot data: snapshot.getChildren()){
                    ArrayList<String> obj = new ArrayList<>();

                    boolean validData = data.child("Odometer_Readings").getChildrenCount() == 10;

                    if (validData)
                    {
                        obj.add(data.child("Odometer_Readings").child("DRIVE_TIME").getValue().toString());
                        obj.add(data.child("Odometer_Readings").child("ODOMETER_IMAGE_URI").getValue().toString());
                        obj.add(data.child("Odometer_Readings").child("OVERRIDE_MILEAGE").getValue().toString());
                        obj.add(data.child("Odometer_Readings").child("OVERRIDE_MILEAGE_URI").getValue().toString());
                        obj.add(data.child("Odometer_Readings").child("STARTED_DRIVING_AT_TIME").getValue().toString());
                        obj.add(data.child("Odometer_Readings").child("STARTING_MILEAGE").getValue().toString());
                        obj.add(data.child("Odometer_Readings").child("TOTAL_MILES_DRIVEN_FROM_GPS").getValue().toString());
                        obj.add(data.child("Odometer_Readings").child("TRIP_TYPE").getValue().toString());
                        obj.add(data.child("Odometer_Readings").child("VEHICLE_TYPE").getValue().toString());
                        obj.add(key);

                        EventInfoObjectForCommUser obj_ = new EventInfoObjectForCommUser(obj);
                        DataArrayList.add(obj_);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        prepareRecyclerView(DataArrayList);
    }

    private void prepareRecyclerView(ArrayList<EventInfoObjectForCommUser> DataArrayList_) {

        adapter = new AdapterForSUC(DataArrayList_,key);

        rv = findViewById(R.id.commUserInfoShowRV);

        rv.setAdapter(adapter);

        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void makeLayoutFullScreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
}