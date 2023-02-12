package com.ellerlabs.tncapp.AdminScreen.CommuteData.ShowChosenUserInfo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventInfoObjectForCommUser {

   String timeStarted;
   String tripType;
   String vehicleType;
   String driveTime;
   String odomStart;
   String odomStartURI;
   String odomEnd;
   String odomEndByUser;
   String odomEndByUserURI;
   String totalKMsDriven;

   String totalKMsDriven_;

   String key;

   public EventInfoObjectForCommUser(ArrayList data) {

      driveTime = data.get(0).toString().trim();
      odomStartURI = data.get(1).toString().trim();
      odomEndByUser = data.get(2).toString().trim();
      odomEndByUserURI = data.get(3).toString().trim();
      timeStarted = data.get(4).toString().trim();
      odomStart = data.get(5).toString().trim();

      DecimalFormat df = new DecimalFormat("#.###");
      totalKMsDriven = (df.format(Double.valueOf(data.get(6).toString().trim())) + " KM");
      totalKMsDriven_ = data.get(6).toString().trim();

      tripType = data.get(7).toString().trim();
      vehicleType = data.get(8).toString().trim();
      key = data.get(8).toString().trim();

      if (!odomStart.contains("N/A"))
      {
         df = new DecimalFormat("#.###");
         odomEnd = df.format(Double.valueOf(odomStart) + Double.valueOf(totalKMsDriven_));

      }
      else{
         odomEnd = "N/A";
      }

   }






}
