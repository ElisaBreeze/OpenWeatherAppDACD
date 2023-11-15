package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class SqliteWeatherStore implements WeatherStore {
    private String path;
    public SqliteWeatherStore(String path) {
        this.path = "jdbc:sqlite:" + path;
    }

    public Connection open(){
        Connection connectionDB;
        try {
            connectionDB = DriverManager.getConnection(this.path);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return connectionDB;
    }

    public void createTables() throws SQLException {
        Connection connection = this.open();
        Statement statement = connection.createStatement();
        Map<String, Location> locationMap = WeatherController.locationLoader();
        for(Map.Entry<String, Location> locationEntry: locationMap.entrySet()){
            String island = locationEntry.getValue().getIsland();
            String sqlCreatesTable = "CREATE TABLE IF NOT EXISTS " + island + " (" +
                    "TimeStamp TEXT PRIMARY KEY," +
                    "Temperature REAL," +
                    "Humidity REAL," +
                    "Precipitation REAL," +
                    "Wind REAL," +
                    "Clouds REAL);";
            statement.execute(sqlCreatesTable);
        }
    }
    @Override
    public void close() throws Exception {
    }

    @Override
    public void save(Weather weather) throws SQLException {
        Connection connection = this.open();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM " + weather.getLocation().getIsland() + " WHERE TimeStamp = '" + weather.getTimeStamp().toString() + "'" + ";";
        if(statement.executeQuery(sql).next()) {
            String updateSQL = "UPDATE " + weather.getLocation().getIsland() +
                    " SET Temperature = " + weather.getTemperature() +
                    ", Humidity = " + weather.getHumidity() +
                    ", Precipitation = " + weather.getPrecipitation() +
                    ", Wind = " + weather.getWind() +
                    ", Clouds = " + weather.getClouds() +
                    " WHERE TimeStamp = '" + weather.getTimeStamp().toString() + "'" + ";";
            statement.execute(updateSQL);
        } else {
            String insertSQL = "INSERT INTO " + weather.getLocation().getIsland() +
                    " VALUES ('" +  weather.getTimeStamp().toString() + "'" +
                    ", " + weather.getTemperature() +
                    ", " + weather.getHumidity() +
                    ", " + weather.getPrecipitation() +
                    ", " + weather.getWind() +
                    ", " + weather.getClouds() + ");";
            statement.execute(insertSQL);
        }

    }
}
