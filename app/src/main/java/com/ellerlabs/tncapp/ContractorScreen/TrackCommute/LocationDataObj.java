package com.ellerlabs.tncapp.ContractorScreen.TrackCommute;

import java.util.ArrayList;

public class LocationDataObj {

    public String latitude;
    public String longitude;
    public String gpsStrength;





    public LocationDataObj(ArrayList data) {
        latitude = data.get(0).toString().trim();
        longitude = data.get(1).toString().trim();
        gpsStrength = data.get(2).toString().trim();


    }
}
