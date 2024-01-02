package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;

import java.sql.*;
import java.util.List;

public class SQLDataStoreManager implements EventStore{

    public Connection open(){
        Connection connectionDB;
        try {
            connectionDB = DriverManager.getConnection("jdbc:sqlite:Datamart.db");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return connectionDB;
    }

    public void createTables() throws SQLException {
        Connection connection = this.open();
        Statement statement = connection.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS events (" +
                "ts TEXT PRIMARY KEY," +
                "location TEXT," +
                "combinedWeatherPredictions TEXT," +
                "hotelInformation TEXT" +
                ")";
            statement.execute(createTableSQL);
        }

    @Override
    public void close(){
    }
    @Override
    public void save(List<JsonObject> eventList) throws StoreException {
        Connection connection = this.open();
        try {
            createTables();
            try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO events (ts, location, combinedWeatherPredictions, hotelInformation) VALUES (?, ?, ?, ?)")) {
                for (JsonObject event : eventList) {
                    String ts = event.get("CombinedWeatherPredictions").getAsJsonObject().get("prediction1").getAsJsonObject().get("ts").getAsString();
                    String location = event.get("Location").getAsString();
                    String combinedWeatherPredictions = event.get("CombinedWeatherPredictions").toString();
                    String hotelInformation = event.get("HotelInformation").toString();

                    insertStatement.setString(1, ts);
                    insertStatement.setString(2, location);
                    insertStatement.setString(3, combinedWeatherPredictions);
                    insertStatement.setString(4, hotelInformation);

                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }
}
