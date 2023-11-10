package org.ulpgc.dacd.control;

public class SqliteWeatherStore implements WeatherStore {
    private String fileName;
    public SqliteWeatherStore(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void close() throws Exception {

    }
}
