package com.example.androidlabs;

/**
 * Object to hold info about Car charging station
 */
public class CarSearchObject {
    /**
     * Title of car charging station
     */
    private String title;
    /**
     * Latitude of car charging station
     */
    private String lat;
    /**
     * Longitude of car charging station
     */
    private String lon;
    /**
     * Phone number of car charging station
     */
    private String telephone;
    /**
     * id of object
     */
    private long id;

    /**
     * Constructor to set info of car charging station
     * @param title Title of car charging station
     * @param lat Latitude of car charging station
     * @param lon Longitude of car charging station
     * @param telephone Phone number of car charging station
     */
    public CarSearchObject(String title, String lat, String lon, String telephone){
        this.title = title;
        this.lat = lat;
        this.lon = lon;
        this.telephone = telephone;
    }

    /**
     * Constructor to set info of car charging station and id of object
     * @param title Title of car charging station
     * @param lat Latitude of car charging station
     * @param lon Longitude of car charging station
     * @param telephone Phone number of car charging station
     * @param id id of object
     */
    public CarSearchObject(String title, String lat, String lon, String telephone, long id){
        this.title = title;
        this.lat = lat;
        this.lon = lon;
        this.telephone = telephone;
        this.id = id;
    }

    /**
     * title getter
     * @return title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Latitude getter
     * @return Latitude
     */
    public String getLat(){
        return lat;
    }

    /**
     * Longitude getter
     * @return Longitude
     */
    public String getLon(){
        return lon;
    }

    /**
     * Phone num getter
     * @return Phone number
     */
    public String getTelephone(){
        return telephone;
    }

    /**
     * id getter id
     * @return
     */
    public long getId(){ return id; }
}
