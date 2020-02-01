package com.example.womensaftey;

import com.google.android.gms.maps.model.LatLng;

public class User {
    public String Username;
    public String gurdianName1;
    public String gurdianName2;
    public String gurdianName3;
    public String gurdianContact1;
    public String gurdianContact2;
    public String gurdianContact3;
    public LatLng latLng;


    public User(String username, String gurdianName1, String gurdianName2, String gurdianName3, String gurdianContact1, String gurdianContact2, String gurdianContact3, LatLng latLng) {
        Username = username;
        this.gurdianName1 = gurdianName1;
        this.gurdianName2 = gurdianName2;
        this.gurdianName3 = gurdianName3;
        this.gurdianContact1 = gurdianContact1;
        this.gurdianContact2 = gurdianContact2;
        this.gurdianContact3 = gurdianContact3;
        this.latLng = latLng;
    }

    public User(){}
}

