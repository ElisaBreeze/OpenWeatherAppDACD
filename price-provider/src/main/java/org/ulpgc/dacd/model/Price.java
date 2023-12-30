package org.ulpgc.dacd.model;

import java.time.Instant;

public class Price {
    private Hotel hotel;
    private double price;
    private Instant ts;
    private String ss;


    public Price(Hotel hotel, Double price, Instant ts, String ss) {
        this.hotel = hotel;
        this.price = price;
        this.ts = ts;
        this.ss = ss;

    }

    public Hotel getHotel() {
        return hotel;
    }

    public double getPrice() {
        return price;
    }

    public Instant getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }
}
