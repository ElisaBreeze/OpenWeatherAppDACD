package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;

import java.util.Map;

public class InformationCreator {
    public static void printInformation(Map<String, JsonObject> bestOptions) {

        //TODO only seems to print 2 of the otpions, and the weather one, doesnt print the price.
        //TODO check the scores and put out of how many? or dont need them 

        int index = 0;
        for (JsonObject event : bestOptions.values()) {
            String hotelName = event.getAsJsonObject("HotelInformation").getAsJsonObject("hotel").get("hotelName").getAsString();
            String location = event.getAsJsonObject("HotelInformation").getAsJsonObject("hotel").get("island").getAsString();

            System.out.println("Hotel Name: " + hotelName);
            System.out.println("Location: " + location);

            if (index == 0) {
                // This is the bestWeatherOption
                printWeatherInformation(event.getAsJsonObject("CombinedWeatherPredictions"));
            } else if (index == 1) {
                // This is the bestPriceOption
                double price = event.getAsJsonObject("HotelInformation").get("price").getAsDouble();
                System.out.println("Price: " + price);
            } else {
                // This is the bestCombinedOption
                printWeatherInformation(event.getAsJsonObject("CombinedWeatherPredictions"));
                double price = event.getAsJsonObject("HotelInformation").get("price").getAsDouble();
                System.out.println("Price: " + price);
            }

            System.out.println("------------------------------");
            index++;
        }
    }

    private static void printWeatherInformation(JsonObject combinedWeatherEvents) {
        System.out.println("Weather Predictions:");

        for (int i = 1; i <= 5; i++) {
            String predictionNumber = "prediction" + i;
            JsonObject prediction = combinedWeatherEvents.getAsJsonObject(predictionNumber);

            String predictionTime = prediction.get("predictionTime").getAsString();
            double temperature = prediction.get("temperature").getAsDouble();
            double humidity = prediction.get("humidity").getAsDouble();
            double precipitation = prediction.get("precipitation").getAsDouble();
            double wind = prediction.get("wind").getAsDouble();
            double clouds = prediction.get("clouds").getAsDouble();

            System.out.println("Day " + i + ":");
            System.out.println("Prediction Time: " + predictionTime);
            System.out.println("Temperature: " + temperature);
            System.out.println("Humidity: " + humidity);
            System.out.println("Precipitation: " + precipitation);
            System.out.println("Wind: " + wind);
            System.out.println("Clouds: " + clouds);
            System.out.println("------------------------------");
        }
    }
}
