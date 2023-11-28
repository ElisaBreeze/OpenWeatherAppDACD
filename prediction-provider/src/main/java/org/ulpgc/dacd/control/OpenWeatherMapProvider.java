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

    public OpenWeatherMapProvider(String apikey)  { this.apikey = apikey; }

    @Override
    public List<Weather> getWeather(Location location) throws IOException { //TODO acortar a menos de 10 líneas
        List<Weather> weatherList = new ArrayList<>();
        String apiCall = apiCall(location);
        HttpURLConnection httpURLConnection = connection(apiCall);

        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String responseInformation = responseReader(httpURLConnection);
            JsonObject jsonObject = new Gson().fromJson(responseInformation, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("list");
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
        }
        return weatherList;
    }

    private String apiCall(Location location) {
        return "https://api.openweathermap.org/data/2.5/forecast" +
                "?lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() +
                "&units=metric" +
                "&appid=" + apikey;
    }

    private HttpURLConnection connection(String apiCall) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(apiCall).openConnection();
        httpURLConnection.setRequestMethod("GET");
        return httpURLConnection;
    }
    private String responseReader(HttpURLConnection httpURLConnection) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}
