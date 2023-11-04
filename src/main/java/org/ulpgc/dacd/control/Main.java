package org.ulpgc.dacd.control;
import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Map<String, Location> locationMap = locationLoader();

        //return qué??
        for(Map.Entry<String, Location> locationEntry: locationMap.entrySet()){
            Instant instant = Instant.now();
            WeatherProvider.getWeather(locationEntry.getValue(), instant, apikeyReader());
        }
    }

        public static Map<String, Location> locationLoader() {
            String filePath = "src/main/resources/Locations.csv";
            Map<String, Location> locationMap = new HashMap<>();

            try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line = "";
                while((line = reader.readLine()) != null){
                    String[] information = line.split("\t");
                    String islandName = information[0];
                    double latitude = Double.parseDouble(information[1]);
                    double longitude = Double.parseDouble(information[2]);

                    Location location = new Location(latitude, longitude);
                    locationMap.put(islandName, location);
                    System.out.println(islandName + "; " + latitude + "; " + longitude);
                }
            } catch(IOException exception) {
                exception.printStackTrace(); //es esta??
            }
            return locationMap;
        }

        public static OpenWeatherMapProvider apikeyReader() {
            String apikeyRead = "";
            OpenWeatherMapProvider apikey = null;

            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/apikey.txt"))) {
                apikeyRead = reader.readLine();
                apikey = new OpenWeatherMapProvider(apikeyRead);
            } catch (IOException e) {
                e.printStackTrace(); //es esta??
            }
            return apikey;
        }
}