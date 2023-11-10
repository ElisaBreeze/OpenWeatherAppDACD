package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class WeatherController {
    private final WeatherProvider weatherProvider;
    private final WeatherStore weatherStore;

    public WeatherController(WeatherProvider weatherProvider, WeatherStore weatherStore) {
        this.weatherProvider = weatherProvider;
        this.weatherStore = weatherStore;
    }
    public WeatherStore getWeatherStore() {
        return weatherStore;
    }
    public WeatherProvider getWeatherProvider() {
        return weatherProvider;
    }

    private void Task() {//throws JsonProcessingException{ TODO añadir en maven; en el pom todas las dependencias que necesito

        Map<String, Location> locationMap = locationLoader();
        for(Map.Entry<String, Location> locationEntry: locationMap.entrySet()){
            Instant instant = Instant.now();
            weatherProvider.getWeather(locationEntry.getValue());
            //TODO guardarlo en el store, en el sqliteweaterstore esta el metodo save => storer.Save()...
        }

    }

    public void runTask() throws IOException{ //TODO añadir el Timer pa que me lo ejecute cuando me interesa
        Task();
    }

    public Map<String, Location> locationLoader() {
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
}
