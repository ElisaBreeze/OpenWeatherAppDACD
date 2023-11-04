package org.ulpgc.dacd.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import org.ulpgc.dacd.model.Location;

public interface WeatherProvider{
    public static String getWeather(Location location, Instant timeStamp, OpenWeatherMapProvider apikey) {

        try {
            String apiCall = "https://api.openweathermap.org/data/2.5/forecast" +
                    "?lat=" + location.getLatitude() +
                    "&lon=" + location.getLongitude() +
                    "&appid=" + apikey.getApikey();

            URL url = new URL(apiCall);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

            httpConnection.setRequestMethod("GET");

            int response = httpConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                String informationLine = "";
                StringBuilder answer = new StringBuilder();

                while ((informationLine = reader.readLine()) != null) {
                    System.out.println(informationLine);
                    answer.append(informationLine);
                }
                reader.close();

                // Print the response
                return answer.toString();
            }
        } catch (IOException exception) {
            //cual??
        }
        return null;
    }
}
