package org.ulpgc.dacd.model;

import java.io.Serializable;

public class Location implements Serializable {
    private double latitude;
    private double longitude;
    private String island;

    public Location(double latitude, double longitude, String island){
        this.latitude = latitude;
        this.longitude = longitude;
        this.island = island;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getIsland() {
        return island;
    }
}
