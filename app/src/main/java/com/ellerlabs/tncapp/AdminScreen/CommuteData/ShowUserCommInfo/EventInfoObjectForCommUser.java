package com.ellerlabs.tncapp.AdminScreen.CommuteData.ShowUserCommInfo;

import java.util.ArrayList;

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

   String key;

   public EventInfoObjectForCommUser(ArrayList data) {

      driveTime = data.get(0).toString().trim();
      odomStartURI = data.get(1).toString().trim();
      odomEndByUser = data.get(2).toString().trim();
      odomEndByUserURI = data.get(3).toString().trim();
      timeStarted = data.get(4).toString().trim();
      odomStart = data.get(5).toString().trim();
      totalKMsDriven= data.get(6).toString().trim() + " KMs";
      tripType = data.get(7).toString().trim();
      vehicleType = data.get(8).toString().trim();
      key = data.get(8).toString().trim();

      if (!odomStart.contains("N/A"))
      {
         odomEnd = String.valueOf(Double.valueOf(odomStart) + Double.valueOf(totalKMsDriven));

      }
      else{
         odomEnd = "N/A";
      }

   }


}
