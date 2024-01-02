package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;

import java.util.Map;

public class InformationCreator {
    public static void printInformation(Map<String, JsonObject> bestOptions) {

        for (Map.Entry<String, JsonObject> entry : bestOptions.entrySet()) {
            String key = entry.getKey();
            JsonObject event = entry.getValue();

            String hotelName = event.getAsJsonObject("HotelInformation").getAsJsonObject("hotel").get("hotelName").getAsString();
            String island = event.getAsJsonObject("HotelInformation").getAsJsonObject("hotel").get("island").getAsString();
            String location = event.getAsJsonObject("HotelInformation").getAsJsonObject("hotel").get("city").getAsString();

            System.out.println("Best " + key + "Option: " + hotelName + ", Located in " + island + " in " + location);

            double price = event.getAsJsonObject("HotelInformation").get("price").getAsDouble();
            System.out.println("Total Price: " + price);
            printWeatherInformation(event.getAsJsonObject("CombinedWeatherPredictions"));

            System.out.println("------------------------------");
        }
    }

        private static void printWeatherInformation(JsonObject combinedWeatherEvents) {
        System.out.println("Weather Predictions per Day:");

        for (int i = 1; i <= 5; i++) {
            String predictionNumber = "prediction" + i;
            JsonObject prediction = combinedWeatherEvents.getAsJsonObject(predictionNumber);
            double temperature = prediction.get("temperature").getAsDouble();
            double humidity = prediction.get("humidity").getAsDouble();
            double precipitation = prediction.get("precipitation").getAsDouble();
            double wind = prediction.get("wind").getAsDouble();
            double clouds = prediction.get("clouds").getAsDouble();

            System.out.println("Day " + i + ":");
            System.out.println("Temperature: " + temperature);
            System.out.println("Humidity: " + humidity);
            System.out.println("Precipitation: " + precipitation + "%");
            System.out.println("Windspeed: " + wind);
            System.out.println("Clouds: " + clouds +"% coverage");
            System.out.println("------------------------------");
        }
    }
}