package com.ellerlabs.tncapp.AdminScreen.CommuteData.ChooseUser;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ellerlabs.tncapp.AdminScreen.CommuteData.ShowChosenUserInfo.ShowUserCommuteInfo;
import com.ellerlabs.tncapp.R;

import java.util.ArrayList;

public class AdapterForSACUser extends
        RecyclerView.Adapter<AdapterForSACUser.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView DisplayNameTV;
        public TextView EmailTV;
        public ImageView arrowBtn;

        public Context context;
        public LinearLayout userInfoBox;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            DisplayNameTV = itemView.findViewById(R.id.timeStarted);
            EmailTV = itemView.findViewById(R.id.totalKMsDriven);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            context = itemView.getContext();
            userInfoBox = itemView.findViewById(R.id.userInfoRow);


        }
    }

    private ArrayList<UserDataObjForSACUser> userDataObjForSACUserArrayList;
    public ProgressBar progressBar_;

    public AdapterForSACUser(ArrayList<UserDataObjForSACUser> userDataObjForSACUserArrayList, ProgressBar progressBar, ShowAndChooseUser showAndChooseUser) {
        this.userDataObjForSACUserArrayList = userDataObjForSACUserArrayList;
        progressBar_ = new ProgressBar(showAndChooseUser);
        progressBar_ = progressBar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.admin_mode_choose_user_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForSACUser.ViewHolder holder, int position) {

        UserDataObjForSACUser data = userDataObjForSACUserArrayList.get(position);

        TextView DisplayNameTV_ = holder.DisplayNameTV;
        DisplayNameTV_.setText(data.DisplayName);

        TextView EmailTV_ = holder.EmailTV;
        EmailTV_.setText(data.Email);

        ImageView arrowBtn = holder.arrowBtn;

        LinearLayout userInfoBox_ = holder.userInfoBox;

        Context context_ = holder.context;

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_.setVisibility(View.VISIBLE);

                Intent intent = new Intent(context_, ShowUserCommuteInfo.class);
                intent.putExtra("Key",data.Key);
                context_.startActivity(intent);
            }
        });

        userInfoBox_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowBtn.performClick();
            }
        });
        userInfoBox_.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    userInfoBox_.setAlpha(0.5f);
                }

                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    userInfoBox_.setAlpha(1f);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userDataObjForSACUserArrayList == null){
            return 0;
        }
        else{
            return userDataObjForSACUserArrayList.size();
        }
    }


}
