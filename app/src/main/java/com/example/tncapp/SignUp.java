package com.example.tncapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView firstName;
    TextView lastName;
    TextView pass;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();




    }

    public void onStart() {
        super.onStart();

        firstName = findViewById(R.id.firstNameET);
        lastName = findViewById(R.id.lastNameET);
        email = findViewById(R.id.emailET);
        pass = findViewById(R.id.passwordET);

        mAuth.createUserWithEmailAndPassword(email.toString().trim(),pass.toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder().setDisplayName(firstName + " " + lastName).build();
                    user.updateProfile(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(SignUp.this, "Sign up incomplete", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                Toast.makeText(SignUp.this, "Sign up failed! Please check internet connection", Toast.LENGTH_SHORT).show();
            }
        });


    }
}