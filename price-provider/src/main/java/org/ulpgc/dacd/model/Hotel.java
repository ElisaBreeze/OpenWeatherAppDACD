package org.ulpgc.dacd.model;

public class Hotel {
    private String hotelName;
    private String island;
    private String city;
    private String hotelKey;

    public Hotel(String hotelName, String island, String city, String hotelKey) {
        this.hotelName = hotelName;
        this.island = island;
        this.city = city;
        this.hotelKey = hotelKey;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getHotelKey() {
        return hotelKey;
    }

    public String getIsland() {
        return island;
    }

    public String getCity() {
        return city;
    }
}
