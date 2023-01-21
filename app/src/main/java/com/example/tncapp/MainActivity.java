package com.example.tncapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private FirebaseUser user;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("UserName");


    TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.Hi);

        mAuth = FirebaseAuth.getInstance();


        user = mAuth.getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        String UID = user.getUid().toString();

        myRef.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Username = snapshot.getValue(String.class);
                if (Username!= null) userName.setText("Hello\n" + Username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void startTrackCommute(View view){
        startActivity(new Intent(this,com.example.tncapp.TrackCommute.trackCommS1.class));
    }

}