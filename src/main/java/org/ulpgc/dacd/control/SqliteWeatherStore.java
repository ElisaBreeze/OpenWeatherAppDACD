package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteWeatherStore implements WeatherStore {
    private String path;
    public SqliteWeatherStore(String path) {
        this.path = "jdbc:sqlite" + path;
    }

    public Connection openDB(){
        Connection connectionDB;
        try {
            connectionDB = DriverManager.getConnection(this.path);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connectionDB;
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void save(Weather weather) {

    }
}
