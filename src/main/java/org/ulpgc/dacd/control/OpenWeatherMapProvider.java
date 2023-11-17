package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class OpenWeatherMapProvider implements WeatherProvider {
    private final String apikey;

    public OpenWeatherMapProvider(String fileName) throws IOException {
        this.apikey = apikeyReader(fileName);
    }

    public static String apikeyReader(String fileName) throws IOException {
        String apikey;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        apikey = reader.readLine();
        return apikey;
    }

    @Override
    public List<Weather> getWeather(Location location) {
        try {
            String apiCall = "https://api.openweathermap.org/data/2.5/forecast" +
                    "?lat=" + location.getLatitude() +
                    "&lon=" + location.getLongitude() +
                    "&units=metric" +
                    "&appid=" + apikey;

            Document weatherDocument = Jsoup.connect(apiCall).ignoreContentType(true).get();
            String information = weatherDocument.text();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(information, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("list");
            List<Weather> weatherList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                Instant timeStamp = Instant.ofEpochSecond(jsonArray.get(i).getAsJsonObject().get("dt").getAsLong());
                if (timeStamp.atZone(ZoneId.systemDefault()).getHour() == 12) {
                    double temperature = jsonArray.get(i).getAsJsonObject().getAsJsonObject("main").get("temp").getAsDouble();
                    double humidity = jsonArray.get(i).getAsJsonObject().getAsJsonObject("main").get("humidity").getAsDouble();
                    double precipitation = jsonArray.get(i).getAsJsonObject().get("pop").getAsDouble();
                    double wind = jsonArray.get(i).getAsJsonObject().getAsJsonObject("wind").get("speed").getAsDouble();
                    int clouds = jsonArray.get(i).getAsJsonObject().getAsJsonObject("clouds").get("all").getAsInt();

                    Weather weather = new Weather(temperature, humidity, precipitation, wind, clouds, timeStamp, location);
                    weatherList.add(weather);
                    if (weatherList.size() == 5) {
                        break;
                    }
                }
            }
            return weatherList;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
