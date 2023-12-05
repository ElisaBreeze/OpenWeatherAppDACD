package org.ulpgc.dacd.model;
import java.io.Serializable;
import java.time.Instant;

public class Weather implements Serializable {
    private double temperature;
    private double humidity;
    private double precipitation;
    private double wind;
    private double clouds;
    private Instant timeStamp;
    private Instant predictionTime;
    private String ss;
    private Location location;

    public Weather(double temperature, double humidity, double precipitation, double wind, double clouds, Instant predictionTime, Location location, Instant timeStamp, String ss) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.wind = wind;
        this.clouds = clouds;
        this.timeStamp = timeStamp;
        this.location = location;
        this.ss = ss;
        this.predictionTime = predictionTime;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public double getTemperature() {
        return temperature;
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

    public double getClouds() {
        return clouds;
    }
    public double getHumidity() {
        return humidity;
    }

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public String getSs() {
        return ss;
    }
}
