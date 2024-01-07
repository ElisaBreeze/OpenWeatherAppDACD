package org.ulpgc.dacd.control;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAnalyzer {
    private static final int GOOD = 3;
    private static final int OK = 1;
    private static final int BAD = 0;
    private double maxWeatherScore = Double.MIN_VALUE;
    private double minPrice = Double.MAX_VALUE;
    private double maxOverallScore = Double.MIN_VALUE;
    private JsonObject bestOverallOption = null;
    private JsonObject bestPriceOption = null;
    private JsonObject bestWeatherOption = null;

    public Map<String, JsonObject> bestOptions(List<JsonObject> eventLists) {
        for (JsonObject event : eventLists) {
            double weatherScore = combinedWeatherScore(event);
            JsonElement priceElement = event.getAsJsonObject("HotelInformation").get("price");
            if (priceElement != null) {
                double hotelPrice = priceElement.getAsDouble();
                if (weatherScore > maxWeatherScore) {
                    maxWeatherScore = weatherScore;
                    bestWeatherOption = event;
                }
                if (hotelPrice < minPrice) {
                    minPrice = hotelPrice;
                    bestPriceOption = event;
                }
                double overallScore = (weatherScore * 0.6 + hotelPrice * 0.4);
                if (overallScore > maxOverallScore) {
                    maxOverallScore = overallScore;
                    bestOverallOption = event;
                }
            }
        }
        return saveOptionsInMap(bestWeatherOption, bestPriceOption, bestOverallOption);
    }



    private Map<String, JsonObject> saveOptionsInMap(JsonObject bestWeatherOption,JsonObject bestPriceOption,JsonObject bestOverallOption){
        Map<java.lang.String, com.google.gson.JsonObject> bestOptionsMap = new HashMap<>();
        bestOptionsMap.put("weather", bestWeatherOption);
        bestOptionsMap.put("price", bestPriceOption);
        bestOptionsMap.put("overall", bestOverallOption);
        return bestOptionsMap;
    }


    private double combinedWeatherScore(JsonObject event) {
        JsonObject combinedWeatherEvents = event.getAsJsonObject("CombinedWeatherPredictions");
        double precipitationScore = averagePrecipitation(combinedWeatherEvents);
        double temperatureScore = averageTemperature(combinedWeatherEvents);
        double cloudinessScore = averageCloudiness(combinedWeatherEvents);
        return (temperatureScore * 0.6) + (precipitationScore * 0.3) + (cloudinessScore * 0.1);
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