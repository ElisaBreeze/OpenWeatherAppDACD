package org.ulpgc.dacd.model;
import java.time.Instant;

public class Weather {

    //setter???
    private double temperature;
    private double rain;
    private double wind;
    private Instant timeStamp;
    private Location location;

    public Weather(double temperature, double rain, double wind, Instant timeStamp, Location location) {
        this.temperature = temperature;
        this.rain = rain;
        this.wind = wind;
        this.timeStamp = timeStamp;
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getRain() {
        return rain;
    }

    public double getWind() {
        return wind;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public Location getLocation() {
        return location;
    }
}
