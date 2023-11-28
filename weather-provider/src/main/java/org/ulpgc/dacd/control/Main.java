package org.ulpgc.dacd.control;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        OpenWeatherMapProvider weatherProvider = new OpenWeatherMapProvider(args[0]);
        JMSWeatherStore weatherStore = new JMSWeatherStore();
        WeatherController weatherController = new WeatherController(weatherProvider, weatherStore);
        weatherController.runTask();
    }
}
