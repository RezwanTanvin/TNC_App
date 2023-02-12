package com.ellerlabs.tncapp.AdminScreen.CommuteData.ChooseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.ellerlabs.tncapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowAndChooseUser extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase fdb;
    DatabaseReference fdbRef;

    static AdapterForSACUser adapter;

    RecyclerView rv;

    ImageButton backButtonCDASCU;

    ProgressBar progressBar;

    TextView showInternetConnTV;

    ArrayList<UserDataObjForSACUser> UserNameAndEmailList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mode_track_commute_data_activity_show_and_choose_user);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fdb = FirebaseDatabase.getInstance();
        fdbRef = fdb.getReference("Commute Data");
        backButtonCDASCU = findViewById(R.id.backButtonCDASCU);
        progressBar = findViewById(R.id.showUserlistProgressBar);
        showInternetConnTV = findViewById(R.id.showInternetConnTV);

        showInternetConnTV.setText(checkInternetConnection(this));

        backButtonCDASCU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        UserNameAndEmailList = new ArrayList<>();

        fdbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(int i =0; i <adapter.getItemCount(); i++){
                    adapter.notifyItemRemoved(i);
                }
                UserNameAndEmailList.clear();

                for (DataSnapshot data: snapshot.getChildren())
                {
                    ArrayList<String> objList = new ArrayList<>();

                    String UIDandUserNameAndEmail = data.getKey();

                    if(UIDandUserNameAndEmail.indexOf("-") != -1)
                    {

                        int index = UIDandUserNameAndEmail.indexOf("-");

                        int index2 = UIDandUserNameAndEmail.lastIndexOf("-");
                        int index3 = UIDandUserNameAndEmail.indexOf("_");

                        String username = (UIDandUserNameAndEmail.substring(index + 1, index2)).trim();
                        String email = (UIDandUserNameAndEmail.substring(index2 + 1, index3) + "." + UIDandUserNameAndEmail.substring(index3 + 1)).trim();

                        objList.add(UIDandUserNameAndEmail);
                        objList.add(username);
                        objList.add(email);

                        UserDataObjForSACUser obj = new UserDataObjForSACUser(objList);

                        UserNameAndEmailList.add(obj);
                    }
                }
                if (UserNameAndEmailList.stream().count()>0){
                    progressBar.setVisibility(View.GONE);
                    showInternetConnTV.setVisibility(View.GONE);

                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        prepareRecyclerView(UserNameAndEmailList);

    }

    private void makeLayoutFullScreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    protected void onStart() {
        super.onStart();
        makeLayoutFullScreen();
        if (UserNameAndEmailList.stream().count()> 0 && checkInternetConnection(this) == "Internet Connected")
        {
            progressBar.setVisibility(View.GONE);
            showInternetConnTV.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            showInternetConnTV.setVisibility(View.VISIBLE);
            showInternetConnTV.setText(checkInternetConnection(this));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserNameAndEmailList.stream().count()> 0 && checkInternetConnection(this) == "Internet Connected")
        {
            progressBar.setVisibility(View.GONE);
            showInternetConnTV.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            showInternetConnTV.setVisibility(View.VISIBLE);
            showInternetConnTV.setText(checkInternetConnection(this));
        }
    }

    public String checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            showInternetConnTV.setVisibility(View.VISIBLE);
            return "Internet Connected";
        } else {
            progressBar.setVisibility(View.GONE);
            return "No Internet Connection";
        }
    }



    private void prepareRecyclerView(ArrayList<UserDataObjForSACUser> UserNameAndEmailList) {

        adapter = new AdapterForSACUser(UserNameAndEmailList,progressBar,this);

        rv = findViewById(R.id.commuteUserRecyclerView);

        rv.setAdapter(adapter);

        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}