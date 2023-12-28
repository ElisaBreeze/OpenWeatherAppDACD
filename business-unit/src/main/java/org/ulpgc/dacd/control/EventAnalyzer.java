package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;

import java.util.List;

public class EventAnalyzer {
    private static final int GOOD = 3;
    private static final int OK = 1;
    private static final int BAD = 0;

    //TODO falta best overall
    public void bestOptions(List<JsonObject> eventLists) {
        double maxWeatherScore = Double.MIN_VALUE;
        double minPrice = Double.MAX_VALUE;
        String bestPriceOption = null;
        String bestWeatherOption = null;

        for (JsonObject event : eventLists) {
            double weatherScore = combinedWeatherScore(event);
            double hotelPrice = event.getAsJsonObject("HotelInformation").get("price").getAsDouble();

            if (weatherScore > maxWeatherScore) {
                maxWeatherScore = weatherScore;
                bestWeatherOption = event.toString();
            }

            if (hotelPrice < minPrice) {
                minPrice = hotelPrice;
                bestPriceOption = event.toString();
            }
        }

        System.out.println("Best Weather Option: " + bestWeatherOption + "with score: " + maxWeatherScore);
        System.out.println("Best Price Option: " + bestPriceOption + "with score: " + minPrice);
    }

    private double combinedWeatherScore(JsonObject event) {
        JsonObject combinedWeatherEvents = event.getAsJsonObject("CombinedWeatherPredictions");
        double precipitationScore = averagePrecipitation(combinedWeatherEvents);
        double temperatureScore = averageTemperature(combinedWeatherEvents);
        double cloudinessScore = averageCloudiness(combinedWeatherEvents);

        return (temperatureScore * 0.5) + (precipitationScore * 0.3) + (cloudinessScore * 0.2); //TODO check if weights are ok
    }

    private double averagePrecipitation(JsonObject combinedWeatherEvents){
        double precipitations = 0.0;
        for (int i = 1; i <= 5; i++) {
            String predictionNumber = "prediction" + i;
            JsonObject prediction = combinedWeatherEvents.getAsJsonObject(predictionNumber);
            double precipitation = prediction.get("precipitation").getAsDouble();
            precipitations += precipitation;
        }
        return scorePrecipitation(precipitations/5);
    }

    private double averageTemperature(JsonObject combinedWeatherEvents){
        double temperatures = 0.0;
        for (int i = 1; i <= 5; i++) {
            String predictionNumber = "prediction" + i;
            JsonObject prediction = combinedWeatherEvents.getAsJsonObject(predictionNumber);
            double temperature = prediction.get("temperature").getAsDouble();
            temperatures += temperature;
        }
        return scoreTemperature(temperatures/5);
    }

    private double averageCloudiness(JsonObject combinedWeatherEvents){
        double cloudinesses = 0.0;
        for (int i = 1; i <= 5; i++) {
            String predictionNumber = "prediction" + i;
            JsonObject prediction = combinedWeatherEvents.getAsJsonObject(predictionNumber);
            double cloudiness = prediction.get("clouds").getAsDouble();
            cloudinesses += cloudiness;
        }
        return scoreCloudiness(cloudinesses/5);
    }

    private static int scoreTemperature(double temperature) {
        if (temperature >= 25.0) {
            return GOOD;
        } else if (temperature >= 20.0) {
            return OK;
        } else {
            return BAD;
        }
    }

    private static int scorePrecipitation(double precipitation) {
        if (precipitation <= 5.0) {
            return GOOD;
        } else if (precipitation <= 20.0) {
            return OK;
        } else {
            return BAD;
        }
    }

    private static int scoreCloudiness(double cloudiness) {
        if (cloudiness <= 20.0) {
            return GOOD;
        } else if (cloudiness <= 40.0) {
            return OK;
        } else {
            return BAD;
        }
    }
    }