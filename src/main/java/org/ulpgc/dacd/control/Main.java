package org.ulpgc.dacd.control;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        OpenWeatherMapProvider weatherProvider = new OpenWeatherMapProvider("src/main/resources/Apikey.txt");
        SqliteWeatherStore weatherStore = new SqliteWeatherStore("src/main/resources/WeatherInformation.db");
        WeatherController weatherController = new WeatherController(weatherProvider, weatherStore);
        weatherController.runTask();
    }
}