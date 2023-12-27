package org.ulpgc.dacd.model;

public class WeatherRating {
    private final String eventData;
    private final double rating;

    public WeatherRating(String eventData, double rating) {
        this.eventData = eventData;
        this.rating = rating;
    }

    public String getEventData() {
        return eventData;
    }

    public double getScore() {
        return rating;
    }
}
