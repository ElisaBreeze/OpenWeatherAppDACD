package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WeatherController {
    private final WeatherProvider weatherProvider;
    private final JMSWeatherStore weatherStore;

    public WeatherController(WeatherProvider weatherProvider, JMSWeatherStore weatherStore) {
        this.weatherProvider = weatherProvider;
        this.weatherStore = weatherStore;
    }

    public WeatherStore getWeatherStore() {
        return weatherStore;
    }

    public void runTask() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    task();
                } catch (StoreExceptions exception) {
                    throw new RuntimeException(exception); //TODO bien??
                }
            }
        }; timer.schedule(timerTask, 0,6*60*60*1000);
    }

    private void task() throws StoreExceptions {
        Map<String, Location> locationMap = locationLoader();
        for (Map.Entry<String, Location> locationEntry : locationMap.entrySet()) {
            try {
                List<Weather> weatherPredictions = weatherProvider.getWeather(locationEntry.getValue());
                for (Weather weather : weatherPredictions) {
                    weatherStore.save(weather);
                }
            } catch (StoreExceptions exception) {
                throw new StoreExceptions(exception.getMessage(), exception);
            }
        }
    }

    public static Map<String, Location> locationLoader() throws StoreExceptions {
        Map<String, Location> locationMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("prediction-provider/src/main/resources/Locations.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] information = line.split("\t");
                Location location = new Location(Double.parseDouble(information[1]),
                        Double.parseDouble(information[2]), information[0]);
                locationMap.put(information[0], location);
            }
        } catch (IOException exception) {
            throw new StoreExceptions(exception.getMessage(), exception);
        }
        return locationMap;
    }
}
