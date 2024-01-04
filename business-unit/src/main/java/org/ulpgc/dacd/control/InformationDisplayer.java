package org.ulpgc.dacd.control;

import java.sql.*;
import java.util.Scanner;

public class InformationDisplayer {
    private static Connection connection;

    public void displayer() throws StoreException {
        databaseConnection();
        showOptions();
        closeConnection();
    }

    private static void databaseConnection() throws StoreException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Datamart.db");
        } catch (Exception exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private static void closeConnection() throws StoreException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private static void showOptions() throws StoreException {
        System.out.println("Welcome! \nDo you want to have a great holiday during the next 5 days in the Canary Islands?");
        System.out.println("Choose to see AVAILABLE HOTELS (1) or BEST OPTIONS (2) by entering the respective number");
        int choice = new Scanner(System.in).nextInt();
        if (choice == 1) {
            showAvailableHotels();
        } else if (choice == 2) {
            showBestOptions();
        } else {
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }

    private static void showAvailableHotels() throws StoreException {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Hotels");
            System.out.println("Here are your options:\n------------------------");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("hotelName"));
            }
            System.out.println("------------------------\nWhich hotel do you want to see information for? Enter the hotel name:");
            String chosenHotel = new Scanner(System.in).nextLine();
            showHotelInformation(chosenHotel);
            statement.close();
        } catch (SQLException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private static void showHotelInformation(String hotelName) throws StoreException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Hotels WHERE hotelName = ?");
            preparedStatement.setString(1, hotelName);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("------------------------\nInformation about the chosen Hotel");
            while (resultSet.next()) {
                System.out.println("Hotel Name: " + resultSet.getString("hotelName"));
                System.out.println("Location: " + resultSet.getString("location"));
                System.out.println("City: " + resultSet.getString("city"));
                System.out.println("Price: " + resultSet.getDouble("price") + "€");
                System.out.println("------------------------\n------------------------");
                showWeatherPredictions(resultSet);
            }
            preparedStatement.close();
        } catch (SQLException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private static void showBestOptions() throws StoreException {
        System.out.println("Choose the Best Option type you would like to see by typing the word: ");
        System.out.println("------------------------");
        System.out.println("price");
        System.out.println("weather");
        System.out.println("overall");
        System.out.println("------------------------");
        String bestOptionChoice = new Scanner(System.in).nextLine();
        showBestOptionInformation(bestOptionChoice);
    }

    private static void showBestOptionInformation(String bestOption) throws StoreException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM BestOptions WHERE option = ?");
            preparedStatement.setString(1, bestOption);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("------------------------\nInformation about the chosen Option:");
            while (resultSet.next()) {
                System.out.println("Hotel Name: " + resultSet.getString("hotelName"));
                System.out.println("Price: " + resultSet.getDouble("price")+ "€");
                System.out.println("------------------------");
                showWeatherPredictions(resultSet);
            }
            preparedStatement.close();
        } catch (SQLException exception) {
            throw new StoreException(exception.getMessage(), exception);
        }
    }

    private static void showWeatherPredictions(ResultSet resultSet) throws SQLException {
        System.out.println("Weather Predictions for the next 5 days in the area:\n------------------------");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Day " + i);
            System.out.println("Temperature: " + resultSet.getDouble("day" + i + "_temperature") + "ºC");
            System.out.println("Precipitation probability: " + resultSet.getDouble("day" + i + "_precipitation") + "%");
            System.out.println("Cloud percentage: " + resultSet.getDouble("day" + i + "_clouds")+ "%");
            System.out.println("------------------------");
        }
        System.out.println("------------------------");
    }
}
