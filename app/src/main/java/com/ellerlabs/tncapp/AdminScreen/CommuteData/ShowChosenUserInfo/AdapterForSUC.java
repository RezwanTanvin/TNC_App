package com.ellerlabs.tncapp.AdminScreen.CommuteData.ShowChosenUserInfo;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ellerlabs.tncapp.AdminScreen.CommuteData.ImagePreview.AD_COMM_ImagePreview;
import com.ellerlabs.tncapp.AdminScreen.CommuteData.ShowMap.googleMapsData;
import com.ellerlabs.tncapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterForSUC extends
        RecyclerView.Adapter<AdapterForSUC.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView timeStarted,totalKMsDriven,tripType,vehicleType,driveTime,odomStart,odomEnd,odomEndByUser,headerOdomStart,headerOdomEnd,headerOdomEndByUser;
        public Button startingMileAge, endingMile, openMap;

        public Context context;
        public LinearLayout firstBox, secondBox,layoutOdomeButtonTitle,layoutOdomeButtons;

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

            headerOdomStart = itemView.findViewById(R.id.textView2684);
            headerOdomEnd = itemView.findViewById(R.id.textView2422);
            headerOdomEndByUser = itemView.findViewById(R.id.textView244);
            layoutOdomeButtonTitle = itemView.findViewById(R.id.odomButtonTitleLayout);
            layoutOdomeButtons = itemView.findViewById(R.id.odomButtonLayout);

            startingMileAge = itemView.findViewById(R.id.showStartingMileageBtn);
            endingMile =  itemView.findViewById(R.id.showEndingMileageByUserBtn);
            openMap = itemView.findViewById(R.id.showMapsBtn);

            context = itemView.getContext();

            firstBox = itemView.findViewById(R.id.firstBox);
            secondBox = itemView.findViewById(R.id.secondBox);

            arrowBtn = itemView.findViewById(R.id.expandWithArrowBtn);
        }
    }
    private ArrayList<EventInfoObjectForCommUser> userDataObjForSUCUserArrayList;
    private String key;

    public AdapterForSUC(ArrayList<EventInfoObjectForCommUser> objArrayList, String key_) {
        this.userDataObjForSUCUserArrayList = objArrayList;
        this.key = key_;
    }
    @NonNull
    @Override
    public AdapterForSUC.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.admin_expand_incident_row, parent, false);

        // Return a new holder instance
        AdapterForSUC.ViewHolder viewHolder = new AdapterForSUC.ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForSUC.ViewHolder holder, int position) {

        EventInfoObjectForCommUser data = userDataObjForSUCUserArrayList.get(position);

        Button startingMileAgeBtn_ = holder.startingMileAge;
        Button endingMileBtn_ =  holder.endingMile;
        Button openMapBtn_ = holder.openMap;

        TextView tripType_ = holder.tripType;
        tripType_.setText(data.tripType);

        TextView vehicleType_ = holder.vehicleType;
        vehicleType_.setText(data.vehicleType);

        TextView driveTime_ = holder.driveTime;
        driveTime_.setText(data.driveTime);

        if (data.odomStart.equals("N/A") && data.odomEndByUser.equals("N/A"))
        {
            holder.layoutOdomeButtonTitle.setVisibility(View.GONE);
            holder.layoutOdomeButtons.setVisibility(View.GONE);
        }


        TextView odomStart_ = holder.odomStart;
        if (data.odomStart.equals("N/A"))
        {
            holder.headerOdomStart.setVisibility(View.GONE);
            odomStart_.setVisibility(View.GONE);

            startingMileAgeBtn_.setVisibility(View.GONE);
        }
        else {
            odomStart_.setText(data.odomStart + " KM");
        }

        TextView odomEnd_ = holder.odomEnd;
        if (data.odomEnd.contains("N/A"))
        {
            holder.headerOdomEnd.setVisibility(View.GONE);
            odomEnd_.setVisibility(View.GONE);
        }
        else {
            odomEnd_.setText(data.odomEnd + " KM");
        }

        TextView odomEndByUser_ = holder.odomEndByUser;
        if (data.odomEndByUser.contains("N/A"))
        {
            holder.headerOdomEndByUser.setVisibility(View.GONE);
            odomEndByUser_.setVisibility(View.GONE);

            endingMileBtn_.setVisibility(View.GONE);
        }
        else {
            odomEndByUser_.setText(data.odomEndByUser + " KM");
        }


        TextView timeStarted_ = holder.timeStarted;
        timeStarted_.setText(formatDate(data.timeStarted));

        TextView totalKMsDriven_ = holder.totalKMsDriven;
        totalKMsDriven_.setText(data.totalKMsDriven);



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
                    flipImageButton(arrowBtn_,0f,180f);
                }
                else
                {
                    secondBox_.setVisibility(View.GONE);
                    flipImageButton(arrowBtn_,180f,360f);
                }
            }
        });
        firstBox_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowBtn_.performClick();
            }
        });

        openMapBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context_, googleMapsData.class);
                intent.putExtra("Key",key);
                intent.putExtra("startTime",data.timeStarted);
                context_.startActivity(intent);
            }
        });

        startingMileAgeBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context_, AD_COMM_ImagePreview.class);
                intent.putExtra("URI",data.odomStartURI);
                context_.startActivity(intent);
            }
        });

        endingMileBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context_, AD_COMM_ImagePreview.class);
                intent.putExtra("URI",data.odomEndByUserURI);
                context_.startActivity(intent);
            }
        });





    }

    public static String formatDate(String date) {
        String[] dateParts = date.split(" ");
        return removeDots(formatTime(dateParts[3])) + " " + dateParts[2] + " " + dateParts[1] + " " + dateParts[5];
    }

    public static String formatTime(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm:ss a");
        try {
            Date date = inputFormat.parse(time);
            return outputFormat.format(date).toUpperCase(Locale.ROOT);
        } catch (Exception e) {
            return "Invalid time format";
        }
    }

    public static String removeDots(String str) {
        return str.replaceAll("\\.", "");
    }

    public void flipImageButton(ImageView imageButton, float from,float to) {
        RotateAnimation animation = new RotateAnimation(from, to,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(250);
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
