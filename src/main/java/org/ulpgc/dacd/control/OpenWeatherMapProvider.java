package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

public class OpenWeatherMapProvider implements WeatherProvider {
    private String apikey;

    public OpenWeatherMapProvider(String fileName) throws IOException {
        this.apikey = apikeyReader(fileName);
    }
    public String getApikey() {
        return apikey;
    }

    public Weather getWeather(Location location) { //time instant es el momento en el que está prevista la prevision
        try {
            String apiCall = "https://api.openweathermap.org/data/2.5/forecast" +
                    "?lat=" + location.getLatitude() +
                    "&lon=" + location.getLongitude() +
                    "&appid=" + apikey;

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
                System.out.println(answer.toString());
            }
        } catch (IOException exception) {
            //cual??
        }
        return null;
    }

    public static String apikeyReader(String fileName) throws IOException {
        String apikey = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            apikey = reader.readLine();
        }
        return apikey;
    }
}
