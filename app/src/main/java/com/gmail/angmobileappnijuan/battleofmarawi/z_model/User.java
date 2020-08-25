package com.gmail.angmobileappnijuan.battleofmarawi.z_model;

import java.util.ArrayList;

public class User {
    private String fbID;
    private int status; //0=pending 1=Admin 2=Active 4=Disabled
    private ArrayList<String> listOfPeers;
    private String firstUsedTimeStamp;
    private String currentTimeStamp;
    private String accessTokenForApp;

    public User() {
    }


}
