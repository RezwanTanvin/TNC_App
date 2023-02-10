package com.ellerlabs.tncapp.AdminScreen.CommuteData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class ShowAndChooseUser extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase fdb;
    DatabaseReference fdbRef;

    static ListDataAdapterForSACUser adapter;

    RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mode_track_commute_data_activity_show_and_choose_user);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fdb = FirebaseDatabase.getInstance();
        fdbRef = fdb.getReference("Commute Data");



        ArrayList<UserDataObjForSACUser> UserNameAndEmailList = new ArrayList<>();

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
                //compareListsAndModifyAdapter(UserNameAndEmailList);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        prepareRecyclerView(UserNameAndEmailList);

    }

    public static void removeItem(int x){
        adapter.notifyItemRemoved(x);
    }

    @Override
    protected void onStart() {
        super.onStart();


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


    }



    private void prepareRecyclerView(ArrayList<UserDataObjForSACUser> UserNameAndEmailList) {

        adapter = new ListDataAdapterForSACUser(UserNameAndEmailList);

        rv = findViewById(R.id.commuteUserRecyclerView);

        rv.setAdapter(adapter);

        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}