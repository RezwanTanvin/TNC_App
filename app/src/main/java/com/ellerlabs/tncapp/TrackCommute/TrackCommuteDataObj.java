package com.ellerlabs.tncapp.TrackCommute;

import java.util.ArrayList;

public class TrackCommuteDataObj {
    public String ID;
    public String VEHICLE_TYPE;
    public String TRIP_TYPE;
    public String STARTING_MILEAGE;
    public String ODOMETER_IMAGE_URI;
    public String STARTED_DRIVING_AT_TIME;
    public String DRIVE_TIME;
    public String TOTAL_MILES_DRIVEN_FROM_GPS;
    public String SQL_TABLE_NAME;
    public String OVERRIDE_MILEAGE;
    public String OVERRIDE_MILEAGE_URI;




    public TrackCommuteDataObj(ArrayList data) {
        ID = data.get(0).toString().trim();
        VEHICLE_TYPE = data.get(1).toString().trim();
        TRIP_TYPE = data.get(2).toString().trim();
        STARTING_MILEAGE = data.get(3).toString().trim();
        ODOMETER_IMAGE_URI = data.get(4).toString().trim();
        STARTED_DRIVING_AT_TIME = data.get(5).toString().trim();
        DRIVE_TIME = data.get(6).toString().trim();
        TOTAL_MILES_DRIVEN_FROM_GPS = data.get(7).toString().trim();
        SQL_TABLE_NAME = data.get(8).toString().trim();
        OVERRIDE_MILEAGE = data.get(9).toString().trim();
        OVERRIDE_MILEAGE_URI = data.get(10).toString().trim();}


    }


