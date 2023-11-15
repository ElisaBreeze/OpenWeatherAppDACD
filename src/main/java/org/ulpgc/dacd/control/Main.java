package org.ulpgc.dacd.control;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        OpenWeatherMapProvider weatherProvider = new OpenWeatherMapProvider(args[0]);
        SqliteWeatherStore weatherStore = new SqliteWeatherStore(args[1]);
        WeatherController weatherController = new WeatherController(weatherProvider, weatherStore);
        weatherController.getWeatherStore().createTables();
        weatherController.runTask();
    }
}
