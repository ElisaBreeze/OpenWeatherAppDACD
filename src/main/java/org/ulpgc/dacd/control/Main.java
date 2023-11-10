package org.ulpgc.dacd.control;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        WeatherProvider weatherProvider = new OpenWeatherMapProvider("src/main/resources/apikey.txt");
        //WeatherStore weatherStore = new SqliteWeatherStore(""); //TODO direccion de la base de datos dentro
        SqliteWeatherStore weatherStore = new SqliteWeatherStore("");
        WeatherController weatherController = new WeatherController(weatherProvider, weatherStore);
        weatherController.runTask();
    }
}