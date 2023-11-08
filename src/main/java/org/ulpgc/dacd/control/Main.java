package org.ulpgc.dacd.control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        WeatherProvider weatherProvider = new OpenWeatherMapProvider("src/main/resources/apikey.txt");
        WeatherStore weatherStore = new WeatherStore(""); //TODO direccion de la base de datos dentro
        WeatherController weatherController = new WeatherController(weatherProvider, weatherStore);
        WeatherController.runTask();

    }


}