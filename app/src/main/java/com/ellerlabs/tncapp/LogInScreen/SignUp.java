package com.ellerlabs.tncapp.LogInScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ellerlabs.tncapp.ContractorScreen.MainActivity;
import com.ellerlabs.tncapp.R;
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

    Button submit;

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
        submit = findViewById(R.id.submitBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!firstName.getText().toString().isEmpty()
                        && !lastName.getText().toString().isEmpty()
                        && !email.getText().toString().isEmpty()
                        && !pass.getText().toString().isEmpty())
                {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {

                                FirebaseUser user = mAuth.getCurrentUser();
                                SQLiteDatabase db = openOrCreateDatabase("LogInInfo",MODE_PRIVATE,null);

                                UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(firstName.getText().toString().trim() + " " + lastName.getText().toString().trim()).build();
                                user.updateProfile(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(SignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                                            ContentValues values = new ContentValues();
                                            values.put("UID", user.getUid().trim());
                                            values.put("UserName", user.getDisplayName().trim());
                                            values.put("email", email.getText().toString().trim());
                                            values.put("password", pass.getText().toString());

                                            db.insert("LogInInfo",null,values);

                                            startActivity(new Intent(SignUp.this, MainActivity.class));
                                        } else {

                                            Toast.makeText(SignUp.this, "Sign up incomplete", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                builder.setTitle("Sign up failed");
                                builder.setMessage("Error message : " + task.getException().getMessage())
                                        .setCancelable(true).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                        }


                    });
                }
                else
                {
                    Toast.makeText(SignUp.this, "One or more fields are empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}