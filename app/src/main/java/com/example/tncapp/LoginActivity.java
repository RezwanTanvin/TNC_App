package com.example.tncapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    ProgressBar loadingProgressBar;

    SQLiteDatabase db;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = openOrCreateDatabase("LogInInfo",MODE_PRIVATE,null);

        //db.execSQL("DROP TABLE LogInInfo");

        db.execSQL("CREATE TABLE IF NOT EXISTS LogInInfo(" +
                "UID varchar(32) NOT NULL PRIMARY KEY ," +
                "UserName varchar(32)," +
                "email varchar(255)," +
                "password varchar(255))");

        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginBtn);
        loadingProgressBar = findViewById(R.id.loading);

        loginButton.setEnabled(false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            loadingProgressBar.setVisibility(View.GONE);

                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser currentuser = mAuth.getCurrentUser();
                            updateUI(currentuser);

                            ContentValues values = new ContentValues();
                            values.put("UID", currentuser.getUid().trim());
                            values.put("UserName", currentuser.getDisplayName().trim());
                            values.put("email", username.trim());
                            values.put("password", password);

                            try
                            {
                                db.execSQL("INSERT OR IGNORE INTO LogInInfo (UID,UserName,email,password) VALUES ('" +
                                        currentuser.getUid().trim() + "','" +
                                        currentuser.getDisplayName().trim() + "','" +
                                        username.trim() + "','" + password + "');"
                                        );
                               // db.insert("LogInInfo",null,values);
                            }
                            catch (Exception e){
                                Log.d(TAG, e.toString());
                            }

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));

                        } else {

                            loadingProgressBar.setVisibility(View.GONE);


                            Toast.makeText(LoginActivity.this, "Authentication failed. " + task.getException()
                                            .getMessage(),
                                    Toast.LENGTH_LONG).show();

                            updateUI(null);
                        }
                    }
                });

            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //loginButton.setClickable(true);
                loginButton.setEnabled(!passwordEditText.getText().toString().isEmpty() && !usernameEditText.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginButton.setEnabled(!passwordEditText.getText().toString().isEmpty() && !usernameEditText.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        usernameEditText.setText("rez@test.com");
        passwordEditText.setText("abcdef");
    }

    private void updateUI(FirebaseUser currentuser) {
        this.user = currentuser;
    }

    @Override
    public void onStart() {
        super.onStart();


        }

        public void goToSignUp (View view) {
        startActivity(new Intent(this, SignUp.class));
        }

    }



