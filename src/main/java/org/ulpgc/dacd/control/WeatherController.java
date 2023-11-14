package org.ulpgc.dacd.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ulpgc.dacd.model.Location;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeatherController {
    private final WeatherProvider weatherProvider;
    private final OpenWeatherMapProvider weatherStore;

    public WeatherController(WeatherProvider weatherProvider, OpenWeatherMapProvider weatherStore) {
        this.weatherProvider = weatherProvider;
        this.weatherStore = weatherStore;
    }
    public OpenWeatherMapProvider getWeatherStore() {
        return weatherStore;
    }

    public WeatherProvider getWeatherProvider() {
        return weatherProvider;
    }

    private void Task() throws JsonProcessingException {

        Map<String, Location> locationMap = locationLoader();
        for(Map.Entry<String, Location> locationEntry: locationMap.entrySet()){

           weatherProvider.getWeather(locationEntry.getValue());
           //TODO como guardar en la base de datos?

        }
    }

    public void runTask() throws IOException{ //TODO añadir el Timer pa que me lo ejecute cuando me interesa
        Task();
        /*
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            Task();
        };
        int cadaCuantasHoras = 6;
        int dias = 5 * 24 / cadaCuantasHoras;
        scheduler.scheduleAtFixedRate(task, 0,cadaCuantasHoras, TimeUnit.HOURS);

        try{
            Thread.sleep(totalEjecuciones * cadaCuantasHoras * 60 * 60 * 1000);
        } catch(InterruptedException exception){
        exception.printStackTrace();
        }
        scheduler.shutdown();
        */
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
