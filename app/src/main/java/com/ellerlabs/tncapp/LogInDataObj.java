package com.ellerlabs.tncapp;

import java.util.ArrayList;

public class LogInDataObj {

    public String UID;
    public String UserName;
    public String email;
    public String password;





    public LogInDataObj(ArrayList data) {
        UID = data.get(0).toString().trim();
        UserName = data.get(1).toString().trim();
        email = data.get(2).toString().trim();
        password = data.get(3).toString();


    }
}
