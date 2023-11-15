package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherController {
    private final WeatherProvider weatherProvider;
    private final SqliteWeatherStore weatherStore;

    public WeatherController(WeatherProvider weatherProvider, SqliteWeatherStore weatherStore) {
        this.weatherProvider = weatherProvider;
        this.weatherStore = weatherStore;
    }
    public SqliteWeatherStore getWeatherStore() {
        return weatherStore;
    }

    public WeatherProvider getWeatherProvider() {
        return weatherProvider;
    }

    private void Task() throws SQLException {

        Map<String, Location> locationMap = locationLoader();
        for(Map.Entry<String, Location> locationEntry: locationMap.entrySet()){
           List<Weather> weatherPredictions =  weatherProvider.getWeather(locationEntry.getValue());
           for (Weather weather : weatherPredictions) {
               weatherStore.save(weather);
           }
        }
    }

    public void runTask() throws SQLException {
        // Task();
       Timer timer = new Timer();
       timer.scheduleAtFixedRate(new TimerTask() {
           /*int days = 1;
           @Override
           public void run() {
               try {
                   Task();
                   days++;
                   if(days == 5) timer.cancel();
               }catch(SQLException exception) {
                   exception.printStackTrace();
               }
           }
       }, 0, TimeUnit.HOURS.toMillis(6));*/

           int count = 1;
           @Override
           public void run() {
               try {
                   Task();
                   count++;
                   if(count == 5) timer.cancel();
               }catch(SQLException exception) {
                   exception.printStackTrace();
               }
           }
       }, 0, TimeUnit.SECONDS.toMillis(6));
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

                Location location = new Location(latitude, longitude, islandName);
                locationMap.put(islandName, location);
            }
        } catch(IOException exception) {
            exception.printStackTrace(); //es esta??
        }
        return locationMap;
    }
}
