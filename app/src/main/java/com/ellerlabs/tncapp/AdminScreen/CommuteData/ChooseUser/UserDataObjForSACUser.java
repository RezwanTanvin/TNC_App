package com.ellerlabs.tncapp.AdminScreen.CommuteData.ChooseUser;

import java.util.ArrayList;

public class UserDataObjForSACUser {

    String Key;
    String DisplayName;
    String Email;

    public UserDataObjForSACUser(ArrayList data){
        Key = data.get(0).toString().trim();
        DisplayName = data.get(1).toString().trim();
        Email = data.get(2).toString().trim();
    }
}
