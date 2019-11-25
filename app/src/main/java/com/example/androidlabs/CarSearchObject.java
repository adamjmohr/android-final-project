package com.example.androidlabs;

public class CarSearchObject {
    private String title;
    private String lat;
    private String lon;
    private String telephone;

    public CarSearchObject(String title, String lat, String lon, String telephone){
        this.title = title;
        this.lat = lat;
        this.lon = lon;
        this.telephone = telephone;
    }

    public String getTitle(){
        return title;
    }

    public String getLat(){
        return lat;
    }

    public String getLon(){
        return lon;
    }

    public String getTelephone(){
        return telephone;
    }
}
