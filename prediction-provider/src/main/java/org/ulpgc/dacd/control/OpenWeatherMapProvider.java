package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenWeatherMapProvider implements WeatherProvider {
    private final String apikey;
    private final String ss = "prediction-provider.OpenWeatherMap";

    public OpenWeatherMapProvider(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public List<Weather> getWeather(Location location) throws StoreExceptions {
        List<Weather> weatherList = new ArrayList<>();
        String apiCall = apiCall(location);
        try {
            HttpURLConnection httpURLConnection = connection(apiCall);
            if (connection(apiCall).getResponseCode() == HttpURLConnection.HTTP_OK) {
                JsonObject jsonObject = new Gson().fromJson(responseReader(httpURLConnection), JsonObject.class);
                arrayProcessing(jsonObject, weatherList, location);
            }
            return weatherList;
        } catch (IOException exception) {
            throw new StoreExceptions(exception.getMessage(), exception);
        }
    }

    private String apiCall(Location location) {
        return "https://api.openweathermap.org/data/2.5/forecast" +
                "?lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() +
                "&units=metric" +
                "&appid=" + apikey;
    }

    private HttpURLConnection connection(String apiCall) throws StoreExceptions {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(apiCall).openConnection();
            httpURLConnection.setRequestMethod("GET");
            return httpURLConnection;
        } catch (IOException exception) {
            throw new StoreExceptions(exception.getMessage(), exception);
        }
    }

    private String responseReader(HttpURLConnection httpURLConnection) throws StoreExceptions {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException exception) {
            throw new StoreExceptions(exception.getMessage(), exception);
        }
    }

    private void arrayProcessing(JsonObject jsonObject, List<Weather> weatherList, Location location) {
        JsonArray jsonArray = jsonObject.getAsJsonArray("list");
        for (int i = 0; i < jsonArray.size() && weatherList.size() < 5; i++) {
            weatherDataProcessing(jsonArray.get(i).getAsJsonObject(), weatherList, location);
        }
    }

    private void weatherDataProcessing(JsonObject jsonArray, List<Weather> weatherList, Location location) {
        Instant predictionTime = Instant.ofEpochSecond(jsonArray.get("dt").getAsLong());
        if (predictionTime.atZone(ZoneId.systemDefault()).getHour() == 12) {
            double temperature = jsonArray.getAsJsonObject("main").get("temp").getAsDouble();
            double humidity = jsonArray.getAsJsonObject("main").get("humidity").getAsDouble();
            double precipitation = jsonArray.get("pop").getAsDouble();
            double wind = jsonArray.getAsJsonObject("wind").get("speed").getAsDouble();
            int clouds = jsonArray.getAsJsonObject("clouds").get("all").getAsInt();
            Weather weather = new Weather(temperature, humidity, precipitation, wind, clouds, predictionTime, location, Instant.now(), ss);
            weatherList.add(weather);
        }
    }
}
