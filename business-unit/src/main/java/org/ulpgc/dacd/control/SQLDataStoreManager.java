package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class SQLDataStoreManager implements EventStore {
    private static final String createHotelTable = "CREATE TABLE IF NOT EXISTS Hotels (" +
            "hotelName TEXT PRIMARY KEY," +
            "checkIn TEXT," +
            "checkOut TEXT," +
            "location TEXT," +
            "city TEXT," +
            "price REAL," +
            "day1_temperature REAL," +
            "day1_precipitation REAL," +
            "day1_clouds REAL," +
            "day2_temperature REAL," +
            "day2_precipitation REAL," +
            "day2_clouds REAL," +
            "day3_temperature REAL," +
            "day3_precipitation REAL," +
            "day3_clouds REAL," +
            "day4_temperature REAL," +
            "day4_precipitation REAL," +
            "day4_clouds REAL," +
            "day5_temperature REAL," +
            "day5_precipitation REAL," +
            "day5_clouds REAL" +
            ")";

    private static final String createBestOptionsTable = "CREATE TABLE IF NOT EXISTS BestOptions (" +
            "option TEXT PRIMARY KEY," +
            "hotelName TEXT," +
            "checkIn TEXT," +
            "checkOut TEXT," +
            "location TEXT," +
            "city TEXT," +
            "price REAL," +
            "day1_temperature REAL," +
            "day1_precipitation REAL," +
            "day1_clouds REAL," +
            "day2_temperature REAL," +
            "day2_precipitation REAL," +
            "day2_clouds REAL," +
            "day3_temperature REAL," +
            "day3_precipitation REAL," +
            "day3_clouds REAL," +
            "day4_temperature REAL," +
            "day4_precipitation REAL," +
            "day4_clouds REAL," +
            "day5_temperature REAL," +
            "day5_precipitation REAL," +
            "day5_clouds REAL" +
            ")";

    private static final String hotelModifier = "INSERT OR REPLACE INTO Hotels " +
            "(hotelName, checkIn, checkOut, location, city, price, " +
            "day1_temperature, day1_precipitation, day1_clouds, " +
            "day2_temperature, day2_precipitation, day2_clouds, " +
            "day3_temperature, day3_precipitation, day3_clouds, " +
            "day4_temperature, day4_precipitation, day4_clouds, " +
            "day5_temperature, day5_precipitation, day5_clouds) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String bestOptionsModifier = "INSERT OR REPLACE INTO BestOptions " +
            "(option, hotelName, checkIn, checkOut, location, city, price, " +
            "day1_temperature, day1_precipitation, day1_clouds, " +
            "day2_temperature, day2_precipitation, day2_clouds, " +
            "day3_temperature, day3_precipitation, day3_clouds, " +
            "day4_temperature, day4_precipitation, day4_clouds, " +
            "day5_temperature, day5_precipitation, day5_clouds) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public Connection open() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:Datamart.db");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void createTables() throws SQLException {
        try (Connection connection = open();
             Statement statement = connection.createStatement()) {
            statement.execute(createHotelTable);
            statement.execute(createBestOptionsTable);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public void save(List<JsonObject> eventList) throws StoreException {
        try (Connection connection = open()) {
            createTables();
            try (PreparedStatement insertStatement = connection.prepareStatement(hotelModifier)) {
                for (JsonObject event : eventList) {
                    JsonObject hotel = event.getAsJsonObject("HotelInformation").getAsJsonObject("hotel");
                    String hotelName = hotel.get("hotelName").getAsString();
                    String location = hotel.get("island").getAsString();
                    String city = hotel.get("city").getAsString();
                    double price = event.getAsJsonObject("HotelInformation").get("price").getAsDouble();
                    String checkIn =event.getAsJsonObject("CombinedWeatherPredictions")
                            .getAsJsonObject("prediction1")
                            .get("predictionTime").getAsString();
                    String checkOut = event.getAsJsonObject("CombinedWeatherPredictions")
                            .getAsJsonObject("prediction5")
                            .get("predictionTime").getAsString();

                    for (int i = 1; i <= 5; i++) {
                        JsonObject weatherPrediction = event.getAsJsonObject("CombinedWeatherPredictions").get("prediction" + i).getAsJsonObject();
                        double temperature = weatherPrediction.get("temperature").getAsDouble();
                        double precipitation = weatherPrediction.get("precipitation").getAsDouble();
                        double clouds = weatherPrediction.get("clouds").getAsDouble();


                        insertStatement.setString(1, hotelName);
                        insertStatement.setString(2, checkIn);
                        insertStatement.setString(3, checkOut);
                        insertStatement.setString(4, location);
                        insertStatement.setString(5, city);
                        insertStatement.setDouble(6, price);
                        insertStatement.setDouble(7 + (i - 1) * 3, temperature);
                        insertStatement.setDouble(8 + (i - 1) * 3, precipitation);
                        insertStatement.setDouble(9 + (i - 1) * 3, clouds);
                    }

                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    public void saveBestOptions(Map<String, JsonObject> bestOptionsMap) throws StoreException {
        try (Connection connection = open();
             PreparedStatement insertStatementBestOptions = connection.prepareStatement(bestOptionsModifier)) {
            System.out.println(bestOptionsMap);
            for (Map.Entry<String, JsonObject> entry : bestOptionsMap.entrySet()) {
                String option = entry.getKey();
                JsonObject bestOption = entry.getValue();
                System.out.println("Best Option "+option + bestOption);

                JsonObject hotel = bestOption.getAsJsonObject("HotelInformation").getAsJsonObject("hotel");
                String hotelName = hotel.get("hotelName").getAsString();
                String location = hotel.get("island").getAsString();
                String city = hotel.get("city").getAsString();
                double price = bestOption.getAsJsonObject("HotelInformation").get("price").getAsDouble();
                String checkIn =bestOption.getAsJsonObject("CombinedWeatherPredictions")
                        .getAsJsonObject("prediction1")
                        .get("predictionTime").getAsString();
                String checkOut = bestOption.getAsJsonObject("CombinedWeatherPredictions")
                        .getAsJsonObject("prediction5")
                        .get("predictionTime").getAsString();

                for (int i = 1; i <= 5; i++) {
                    JsonObject weatherPrediction = bestOption.getAsJsonObject("CombinedWeatherPredictions").get("prediction" + i).getAsJsonObject();
                    double temperature = weatherPrediction.get("temperature").getAsDouble();
                    double precipitation = weatherPrediction.get("precipitation").getAsDouble();
                    double clouds = weatherPrediction.get("clouds").getAsDouble();

                    insertStatementBestOptions.setString(1, option);
                    insertStatementBestOptions.setString(2, hotelName);
                    insertStatementBestOptions.setString(3, checkIn);
                    insertStatementBestOptions.setString(4, checkOut);
                    insertStatementBestOptions.setString(5, location);
                    insertStatementBestOptions.setString(6, city);
                    insertStatementBestOptions.setDouble(7, price);
                    insertStatementBestOptions.setDouble(8 + (i - 1) * 3, temperature);
                    insertStatementBestOptions.setDouble(9 + (i - 1) * 3, precipitation);
                    insertStatementBestOptions.setDouble(10 + (i - 1) * 3, clouds);
                }
                insertStatementBestOptions.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

}