package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class OpenWeatherMapProvider implements WeatherProvider {
    private String apikey;

    public OpenWeatherMapProvider(String fileName) throws IOException {
        this.apikey = apikeyReader(fileName);
    }

    public String getApikey() {
        return apikey;
    }

    public static String apikeyReader(String fileName) throws IOException {
        String apikey = "";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        apikey = reader.readLine();
        return apikey;
    }

    @Override
    public Weather getWeather(Location location) { //time instant es el momento en el que está prevista la prevision
        try {
            String apiCall = "https://api.openweathermap.org/data/2.5/forecast" +
                    "?lat=" + location.getLatitude() +
                    "&lon=" + location.getLongitude() +
                    "&appid=" + apikey;

            Document weatherDocument = Jsoup.connect(apiCall).ignoreContentType(true).get();
            String information = weatherDocument.text();

            //TODO quitar los sout??

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(information, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("list");
            System.out.println(jsonArray.toString());

            double temperature = jsonArray.get(0).getAsJsonObject().getAsJsonObject("main").get("temp").getAsDouble();
            System.out.println("temperature: " +  temperature);
            double humidity = jsonArray.get(0).getAsJsonObject().getAsJsonObject("main").get("humidity").getAsDouble();
            System.out.println("humidity: " +  humidity);

            //TODO bien?? siempre sale cero
            double precipitation = jsonArray.get(0).getAsJsonObject().get("pop").getAsDouble();
            System.out.println("Precipitation percentage: " + precipitation);

            double wind = jsonArray.get(0).getAsJsonObject().getAsJsonObject("wind").get("speed").getAsDouble();
            System.out.println("Wind: " + wind);

            int clouds = jsonArray.get(0).getAsJsonObject().getAsJsonObject("clouds").get("all").getAsInt();
            System.out.println("Cloud percentage: " + clouds);

            Weather weather = new Weather();
            weather.setLocation(location);
            weather.setTemperature(temperature);
            weather.setPrecipitation(precipitation);
            weather.setHumidity(humidity);
            weather.setClouds(clouds);
            weather.setWind(wind);
            //TODO time??
            return weather;

        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}