package com.ellerlabs.tncapp.AdminScreen.CommuteData.ShowUserCommInfo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ellerlabs.tncapp.AdminScreen.CommuteData.ListDataAdapterForSACUser;
import com.ellerlabs.tncapp.AdminScreen.CommuteData.UserDataObjForSACUser;
import com.ellerlabs.tncapp.R;

import java.util.ArrayList;

public class ListDataAdapterForSUC extends
        RecyclerView.Adapter<ListDataAdapterForSUC.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView timeStarted,totalKMsDriven,tripType,vehicleType,driveTime,odomStart,odomEnd,odomEndByUser;
        public Button startingMileAge, endingMile, openMap;

        public Context context;
        public LinearLayout firstBox, secondBox;

        public ImageView arrowBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeStarted = itemView.findViewById(R.id.timeStarted);
            totalKMsDriven = itemView.findViewById(R.id.totalKMsDriven);
            tripType = itemView.findViewById(R.id.tripType);
            vehicleType = itemView.findViewById(R.id.vehicleType);
            driveTime = itemView.findViewById(R.id.driveTime);
            odomStart = itemView.findViewById(R.id.odometerAtStart);
            odomEnd = itemView.findViewById(R.id.odometerAtEnd);
            odomEndByUser = itemView.findViewById(R.id.odometerAtEndByUser);

            startingMileAge = itemView.findViewById(R.id.startTaskBtn);
            endingMile =  itemView.findViewById(R.id.endCommuteBtn);
            openMap = itemView.findViewById(R.id.showMapsBtn);

            context = itemView.getContext();

            firstBox = itemView.findViewById(R.id.firstBox);
            secondBox = itemView.findViewById(R.id.secondBox);

            arrowBtn = itemView.findViewById(R.id.expandWithArrowBtn);
        }
    }
    private ArrayList<EventInfoObjectForCommUser> userDataObjForSUCUserArrayList;

    public ListDataAdapterForSUC (ArrayList<EventInfoObjectForCommUser> objArrayList) {
        this.userDataObjForSUCUserArrayList = objArrayList;
    }
    @NonNull
    @Override
    public ListDataAdapterForSUC.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.admin_expand_incident_row, parent, false);

        // Return a new holder instance
        ListDataAdapterForSUC.ViewHolder viewHolder = new ListDataAdapterForSUC.ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListDataAdapterForSUC.ViewHolder holder, int position) {

        EventInfoObjectForCommUser data = userDataObjForSUCUserArrayList.get(position);

        TextView tripType_ = holder.tripType;
        tripType_.setText(data.tripType);

        TextView vehicleType_ = holder.vehicleType;
        vehicleType_.setText(data.vehicleType);

        TextView driveTime_ = holder.driveTime;
        driveTime_.setText(data.driveTime);

        TextView odomStart_ = holder.odomStart;
        odomStart_.setText(data.odomStart);

        TextView odomEnd_ = holder.odomEnd;
        odomEnd_.setText(data.odomEnd);

        TextView odomEndByUser_ = holder.odomEndByUser;
        odomEndByUser_.setText(data.odomEndByUser);

        TextView timeStarted_ = holder.timeStarted;
        timeStarted_.setText(data.timeStarted);

        TextView totalKMsDriven_ = holder.totalKMsDriven;
        totalKMsDriven_.setText(data.totalKMsDriven);

        Button startingMileAge_ = holder.startingMileAge;
        Button endingMile_ =  holder.endingMile;
        Button openMap_ = holder.openMap;

        ImageView arrowBtn_ = holder.arrowBtn;

        LinearLayout firstBox_ = holder.firstBox;
        LinearLayout secondBox_ = holder.secondBox;

        Context context_ = holder.context;

        arrowBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(secondBox_.getVisibility()== View.GONE)
                {
                secondBox_.setVisibility(View.VISIBLE);
                    flipImageButton(arrowBtn_);
                }
                else
                {
                    secondBox_.setVisibility(View.GONE);
                    flipImageButton(arrowBtn_);
                }
            }
        });





    }

    public void flipImageButton(ImageView imageButton) {
        RotateAnimation animation = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(500);
        animation.setFillAfter(true);
        imageButton.startAnimation(animation);
    }



    @Override
    public int getItemCount() {
        if (userDataObjForSUCUserArrayList == null){
            return 0;
        }
        else{
            return userDataObjForSUCUserArrayList.size();
        }
    }


}
